package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Cart;
import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.entity.Order;
import com.peter.springboot.store.entity.Product;
import com.peter.springboot.store.service.OrderService;
import com.peter.springboot.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/store")
public class ProductController {

    @Autowired
    private final ProductService proSer;

    @Autowired
    private final OrderService orSer;

    public ProductController(ProductService ser, OrderService orSer) {
        this.proSer = ser;
        this.orSer = orSer;
    }

    @GetMapping("/list")
    public String getAllProducts(Model model, HttpServletRequest request) {
        String msg = request.getParameter("thank_message");
        if (msg != null) {
            System.out.println(msg);
            model.addAttribute("message_store", msg);
        }
        model.addAttribute("searchValue", "_");
        return listByPage(model, 1, "_", "exportPrice", "asc", "false");
    }

    @GetMapping("/page/{pageNumber}/{searchValue}")
    public String listByPage(Model model,
                             @PathVariable("pageNumber") int currentPage,
                             @PathVariable("searchValue") String searchValue,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("reverse") String reverse) {
        String url = "store/list-products";
        try {
            if (reverse.equals("true")) {
                sortDir = sortDir.equals("asc") ? "desc" : "asc";
            }
            Page<Product> page = proSer.showProductStore(currentPage, sortField, sortDir);
            List<Product> list = page.getContent();
            model.addAttribute("products", list);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverse", reverse);
        } catch (Exception ex) {
            url = "error-page";
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("searchValue") String productName, Model model) {
        Page<Product> page = proSer.findByProductNameContaining(productName, 1, "exportPrice", "asc");
        List<Product> list = page.getContent();
        model.addAttribute("searchValue", productName);
        if (list.isEmpty()) {
            model.addAttribute("message_store", "Can not find this product!");
            model.addAttribute("products", list);
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalItems", 0);
            model.addAttribute("sortField", "productName");
            model.addAttribute("sortDir", "asc");
            model.addAttribute("reverse", "true");
        } else {
            model.addAttribute("products", list);
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("sortField", "exportPrice");
            model.addAttribute("sortDir", "asc");
            model.addAttribute("reverse", "true");
            model.addAttribute("reverseSortDir", "asc");
        }
        return "store/list-products";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("productId") int productId, @RequestParam("quantityBuy") int quantityBuy,
                            HttpServletRequest request, Model model) {
        String url = "error-page";
        try {
            HttpSession session = request.getSession();
            Customer userLogin = (Customer) session.getAttribute("userLogin");
            String msg = "";
            if (userLogin == null) {
                url = "redirect:/login";
                msg = "Sorry, you must login before buying product to cart!";
                model.addAttribute("LOGIN_MESSAGE", msg);
            } else if (userLogin.getRoleId().equals("Admin")) {
                url = "store/list-products";
                msg = "Admin can't buy product !!!";
            } else if (!(userLogin.getRoleId().equals("User") || userLogin.getRoleId().equals("Admin"))) {
                url = "store/list-products";
                msg = "Your role is not supported !!!";
            } else { // User role
                msg = "Nothing!";
                url = "store/list-products";
                Product proInStock = proSer.getProductById(productId);
                if (quantityBuy <= 0) {
                    msg = "Quantity buy must be more than zero, please!";
                } else if (proInStock.getQuantityInStock() < quantityBuy) {
                    msg = "Sorry, we don't have enough quantity for " + proInStock.getProductName() + " product!";
                } else {
                    Cart cart = (Cart) session.getAttribute("cart");
                    Order order = (Order) session.getAttribute("order");
                    if (order == null) {
                        Customer c = (Customer) session.getAttribute("userLogin");
                        order = new Order(c);
                        order.setDiscount(); // random discount belong to points customer
                        System.out.println("Created new order successfully! Order date: "
                                + order.getOrderDate() + ", order id: " + order);
                    }
                    if (cart == null) {
                        cart = new Cart();
                    }
                    Product tmp = new Product(productId, proInStock.getCategory(), proInStock.getProductName(),
                            quantityBuy, proInStock.getImportPrice(), proInStock.getExportPrice(), proInStock.isStatus());
                    cart.addProduct(tmp.getId(), tmp);
                    msg = "Added " + quantityBuy + " '" + tmp.getProductName() + "' successfully!";
                    session.setAttribute("total", Math.round(cart.getTotal() * (1 - order.getDiscount()) * 100.0) / 100.0);
                    order.setTotal(cart.getTotal());
                    orSer.saveOrder(order);
                    session.setAttribute("order", order);
                    session.setAttribute("cart", cart);
                }
            }
            url = getAllProducts(model, request);
            model.addAttribute("message_store", msg);
        } catch (Exception ex) {
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/showCart")
    public String showCart(HttpServletRequest request, Model model) {
        String url = "store/cart";
        try {
            HttpSession session = request.getSession();
            Customer userLogin = (Customer) session.getAttribute("userLogin");
            Cart cart = (Cart) session.getAttribute("cart");
            Order order = (Order) session.getAttribute("order");
            if (userLogin != null && userLogin.getRoleId().equals("User")) {
                if (cart != null && order != null) {
                    double total = cart.getTotal();
                    session.setAttribute("total", Math.round(total * (1 - order.getDiscount()) * 100.0) / 100.0);
                } else {
                    url = getAllProducts(model, request);
                    model.addAttribute("message_store",
                            "You did not buy anything here!");
                }
            } else {
                model.addAttribute("message_store",
                        "Please create or login with user account!");
                url = "redirect:/login";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/updateProductCart")
    public String updateProductCart(HttpServletRequest request, Model model,
                                    @RequestParam("productId") int productId, @RequestParam("quantityBuy") int quantityBuy) {
        String url = "error-page";
        String msg = "";
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Order order = (Order) session.getAttribute("order");
        try {
            Product tmp = proSer.getProductById(productId);
            if (quantityBuy == 0) {
                cart.deleteProduct(productId);
                msg = "Deleted book '" + tmp.getProductName() + "' successfully!";
            } else {
                tmp.setQuantityInStock(quantityBuy);
                cart.updateProduct(productId, tmp);
                msg = "Updated book '" + tmp.getProductName() + "' successfully!";
            }
            url = "store/cart";
            model.addAttribute("message_cart", msg);
            session.setAttribute("cart", cart);
            session.setAttribute("total", Math.round(cart.getTotal() * (1 - order.getDiscount()) * 100.0) / 100.0);
            order.setTotal(cart.getTotal());
            session.setAttribute("order", order);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/deleteProductCart")
    public String deleteProductCart(HttpServletRequest request, Model model,
                                    @RequestParam("productId") int productId) {
        String url = "error-page";
        String msg = "";
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Order order = (Order) session.getAttribute("order");
        try {
            Product tmp = cart.deleteProduct(productId);
            msg = "Deleted book '" + tmp.getProductName() + "' successfully!";
            url = "store/cart";
            model.addAttribute("message_cart", msg);
            session.setAttribute("cart", cart);
            session.setAttribute("total", Math.round(cart.getTotal() * (1 - order.getDiscount()) * 100.0) / 100.0);
            order.setTotal(cart.getTotal());
            session.setAttribute("order", order);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }
}




