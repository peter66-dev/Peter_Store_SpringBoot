package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.entity.Order;
import com.peter.springboot.store.entity.Product;
import com.peter.springboot.store.service.ProductService;
import org.springframework.lang.UsesSunMisc;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/store")
public class ProductController {

    private ProductService proSer;

    public ProductController(ProductService ser) {
        this.proSer = ser;
    }

    @GetMapping("/list")
    public String getAllProducts(Model model) {
        List<Product> list = proSer.findByStatusTrue();
        model.addAttribute("products", list);
        return "store/list-products";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("searchValue") String productName, Model model) {
        List<Product> list = proSer.findByProductNameContaining(productName);
        model.addAttribute("products", list);
        return "store/list-products";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("productId") int productId,
                            HttpServletRequest request, Model model) {
        String url = "";
        try {
            HttpSession session = request.getSession();
            Customer userLogin = (Customer) session.getAttribute("userLogin");
            String msg = "";
            if (userLogin == null) {
                url = "redirect:/login";
                msg = "Sorry, you must login before buying product to cart!";
            } else if (userLogin.getRoleId().equals("Admin")) {
                url = "store/list-products";
                msg = "Admin can't buy product !!!";
            } else if (!(userLogin.getRoleId().equals("User") || userLogin.getRoleId().equals("Admin"))) {
                url = "store/list-products";
                msg = "Your role is not supported !!!";
            } else { // User role
                double total = 0;
                url = "store/list-products";
                ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
                Product proInStock = proSer.getProductById(productId);
                msg = "Nothing!";
                Order order = (Order) session.getAttribute("order");
                if (order == null) {
                    Customer c = (Customer) session.getAttribute("userLogin");
                    order = new Order(c);
                    System.out.println("Create new order successfully! Order date: "
                            + order.getOrderDate() + ", order id: " + order);
                }
                if (cart == null || cart.isEmpty()) {
                    Product tmp = new Product(proInStock.getId(), proInStock.getCategory(),
                            proInStock.getProductName(), 0, proInStock.getImportPrice(), proInStock.getExportPrice());
                    cart = new ArrayList<Product>();
                    tmp.setQuantityInStock(1);
                    cart.add(tmp);
                    msg = "Created a new cart! Adding '" + tmp.getProductName() + "' successfully!";
                } else {
                    boolean check = false;
                    for (Product pro : cart) {
                        if (productId == pro.getId()) {
                            total += pro.getQuantityInStock() * pro.getExportPrice();
                            check = !check;
                            pro.setQuantityInStock(pro.getQuantityInStock() + 1);
                            msg = "Set a new quantity for '" + pro.getProductName() + "' successfully!";
                        }
                    }
                    if (!check) {
                        Product tmp = new Product(proInStock.getId(), proInStock.getCategory(),
                                proInStock.getProductName(), 1, 0, proInStock.getExportPrice());
                        msg = "Adding a new book '" + tmp.getProductName() + "' for cart successfully!";
                        cart.add(tmp);
                    }
                }
                session.setAttribute("total", Math.round(total * 100.0) / 100.0);
                order.setTotal(total);
                session.setAttribute("order", order);
                session.setAttribute("cart", cart);
                model.addAttribute("products", proSer.findByStatusTrue());
            }
            model.addAttribute("message_store", msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return url;
    }

    @GetMapping("/showCart")
    public String showCart(HttpServletRequest request, Model model) {
        String url = "store/cart";
        try {
            HttpSession session = request.getSession();
            Customer userLogin = (Customer) session.getAttribute("userLogin");
            ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
            if (userLogin != null && userLogin.getRoleId().equals("User")) {
                double total = 0;
                if(!cart.isEmpty()){
                    for (Product p : cart) {
                        total += p.getQuantityInStock() * p.getExportPrice();
                    }
                }
                session.setAttribute("total", total);
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

    @GetMapping("/subProductCart")
    public String subProductCart(HttpServletRequest request, Model model,
                                 @RequestParam("productId") int productId) {
        String msg = "";
        HttpSession session = request.getSession();
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
        try {
            int quantityBuy = 0;
            String proName = "";
            Product tmp = null;
            Order order = (Order) session.getAttribute("order");
            for (Product p : cart) {
                if (p.getId() == productId) {
                    p.setQuantityInStock(p.getQuantityInStock() - 1);
                    quantityBuy = p.getQuantityInStock();
                    proName = p.getProductName();
                    if (quantityBuy == 0) {
                        tmp = p;
                        msg = "Deleting book '" + p.getProductName() + "' successfully!";
                    }
                }
            }
            if (tmp != null) {
                cart.remove(tmp);
            }
            if (quantityBuy > 0) {
                msg = "Subtracting -1 '" + proName + "' successfully!";
            }
            session.setAttribute("cart", cart);
            session.setAttribute("total", getTotal(cart));
            order.setTotal(getTotal(cart));
            session.setAttribute("order", order);
            model.addAttribute("message_cart", msg);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error_message", ex.getMessage());
        }
        return "store/cart";
    }

    @GetMapping("/addProductCart")
    public String addProductCart(HttpServletRequest request, Model model, @RequestParam("productId") int productId) {
        String url = "store/cart";
        try {
            double total = 0;
            Product proInStock = proSer.getProductById(productId);
            HttpSession session = request.getSession();
            ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
            Order order = (Order) session.getAttribute("order");
            for (Product p : cart) {
                String msg = "";
                if (p.getId() == productId) {
                    if (p.getQuantityInStock() == proInStock.getQuantityInStock()) {
                        msg = "Sorry, we don't have enough quantity for this book!";
                        model.addAttribute("message_cart", msg);
                    }
                    p.setQuantityInStock(p.getQuantityInStock() + 1);
                    msg = "Update new quantity for '" + p.getProductName() + "' successfully!";
                    model.addAttribute("message_cart", msg);
                }
                total += p.getQuantityInStock() * p.getExportPrice();
            }
            session.setAttribute("total", total);
            session.setAttribute("cart", cart);
            order.setTotal(getTotal(cart));
            session.setAttribute("order", order);
        } catch (Exception ex) {
            url = "error-page";
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
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
        Order order = (Order) session.getAttribute("order");
        try {
            Product tmp = null;
            for (Product p : cart) {
                if (p.getId() == productId) {
                    msg = "Deleting book '" + p.getProductName() + "' successfully!";
                    url = "store/cart";
                    tmp = p;
                }
            }
            cart.remove(tmp);
            model.addAttribute("message_cart", msg);
            session.setAttribute("cart", cart);
            session.setAttribute("total", getTotal(cart));
            order.setTotal(getTotal(cart));
            session.setAttribute("order", order);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    public double getTotal(ArrayList<Product> cart) {
        double total = 0;
        for (Product p : cart) {
            total += p.getQuantityInStock() * p.getExportPrice();
        }
        return total;
    }

}




