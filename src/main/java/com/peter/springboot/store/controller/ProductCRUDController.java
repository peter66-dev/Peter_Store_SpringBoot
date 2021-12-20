package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.entity.Product;
import com.peter.springboot.store.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductCRUDController {

    private ProductService proSer;

    public ProductCRUDController(ProductService proSer) {
        this.proSer = proSer;
    }

    @GetMapping("/list")
    public String getAllProduct(HttpServletRequest request, Model model) {
        String url = "admin/check-products";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                List<Product> list = proSer.getAllProducts();
                model.addAttribute("products", list);
            } else {
                url = "error-page";
                model.addAttribute("error_message", "Login with role admin before update product, please!");
            }
        } catch (Exception ex) {
            url = "error-page";
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/showFormUpdateProduct")
    public String updateProduct(HttpServletRequest request, Model model, @RequestParam("productId") int productId) {
        String url = "admin/form-product";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                Product pro = proSer.getProductById(productId);
                model.addAttribute("product_update", pro);
            } else {
                url = "error-page";
                model.addAttribute("error_message", "Login with role admin before update product, please!");
            }
        } catch (Exception ex) {
            url = "error-page";
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @PostMapping("/save")
    public String saveProduct(HttpServletRequest request, Model model, @ModelAttribute("product_update") Product product_update) {
        String url = "admin/check-products";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                if (product_update.getImportPrice() < product_update.getExportPrice()) {
                    if (product_update.getQuantityInStock() <= 0) {
                        product_update.setQuantityInStock(0);
                        product_update.setStatus(false);
                        proSer.saveProduct(product_update);
                    }
                    else{
                        product_update.setStatus(true);
                        proSer.saveProduct(product_update);
                    }
                    model.addAttribute("products", proSer.getAllProducts());
                    model.addAttribute("products_page_message",
                            "Updated '" + product_update.getProductName() + "' successfully");
                } else {
                    url = "error-page";
                    model.addAttribute("error_message", "Import price must be less than export price!");
                }
            } else {
                url = "error-page";
                model.addAttribute("error_message", "Login with role admin before update product, please!");
            }
        } catch (Exception ex) {
            url = "error-page";
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(HttpServletRequest request, Model model,
                                @RequestParam("productId") int productId) {
        String url = "admin/check-products";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                Product pro = proSer.getProductById(productId);
                pro.setStatus(false);
                proSer.saveProduct(pro);
                model.addAttribute("products", proSer.getAllProducts());
                model.addAttribute("products_page_message",
                        "Set off status product '" + pro.getProductName() + "' successfully");
            } else {
                url = "error-page";
                model.addAttribute("error_message", "Login with role admin before update product, please!");
            }
        } catch (Exception ex) {
            url = "error-page";
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/search")
    public String searchProduct(HttpServletRequest request, Model model,
                                @RequestParam("searchValue") String searchValue) {
        String url = "admin/check-products";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                List<Product> list = proSer.findByProductNameContaining(searchValue);
                if(list.isEmpty()){
                    model.addAttribute("products_page_message", "Can not find this product!");
                }else{
                    model.addAttribute("products", list);
                }
            } else {
                url = "error-page";
                model.addAttribute("error_message", "Login with role admin before update product, please!");
            }
        } catch (Exception ex) {
            url = "error-page";
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

}
