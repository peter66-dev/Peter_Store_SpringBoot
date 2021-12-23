package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.entity.Product;
import com.peter.springboot.store.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductCRUDController {

    private final ProductService proSer;

    public ProductCRUDController(ProductService proSer) {
        this.proSer = proSer;
    }

    @GetMapping("")
    public String listProduct(HttpServletRequest request, Model model) {
        model.addAttribute("searchValue", "_");
        return listByPage(request, model, 1, "_", "id", "asc", "false");
    }

    @GetMapping("/page/{pageNumber}/{searchValue}")
    public String listByPage(HttpServletRequest request, Model model,
                             @PathVariable("pageNumber") int currentPage,
                             @PathVariable("searchValue") String searchValue,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("reverse") String reverse) {
        String url = "admin/check-products";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin != null) {
                if (admin.getRoleId().equals("Admin")) {
                    if (reverse.equals("true")) {
                        sortDir = sortDir.equals("asc") ? "desc" : "asc";
                    }
                    Page<Product> page = proSer.findByProductNameContaining(searchValue, currentPage, sortField, sortDir);
                    List<Product> list = page.getContent();
                    model.addAttribute("products", list);
                    model.addAttribute("currentPage", currentPage);
                    model.addAttribute("totalPages", page.getTotalPages());
                    model.addAttribute("totalItems", page.getTotalElements());
                    model.addAttribute("sortField", sortField);
                    model.addAttribute("sortDir", sortDir);
                    model.addAttribute("reverse", reverse);
                } else {
                    url = "error-page";
                    model.addAttribute("error_message", "Login with role admin before update product, please!");
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


    @GetMapping("/showFormUpdateProduct")
    public String updateProduct(HttpServletRequest request, Model model, @RequestParam("productId") int productId) {
        String url = "admin/form-product";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin != null) {
                if (admin.getRoleId().equals("Admin")) {
                    Product pro = proSer.getProductById(productId);
                    model.addAttribute("product_update", pro);
                } else {
                    url = "error-page";
                    model.addAttribute("error_message", "Login with role admin before update product, please!");
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

    @GetMapping("/showFormAddProduct")
    public String showFormAddProduct(HttpServletRequest request, Model model) {
        String url = "admin/form-product";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin != null) {
                if (admin.getRoleId().equals("Admin")) {
                    Product pro = new Product();
                    model.addAttribute("product_update", pro);
                } else {
                    url = "error-page";
                    model.addAttribute("error_message", "Login with role admin before update product, please!");
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

    @PostMapping("/save")
    public String saveProduct(HttpServletRequest request, Model model,
                              @ModelAttribute("product_update") Product product_update) {
        String url = "";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            int id = product_update.getId();
            if (admin.getRoleId().equals("Admin")) {
                if (product_update.getImportPrice() < product_update.getExportPrice()) {
                    if (product_update.getQuantityInStock() <= 0) {
                        product_update.setQuantityInStock(0);
                        product_update.setStatus(false);
                        proSer.saveProduct(product_update);
                    } else {
                        product_update.setStatus(true);
                        proSer.saveProduct(product_update);
                    }
                    url = listProduct(request, model);
                    if (id == 0) {
                        model.addAttribute("products_page_message",
                                "Created '" + product_update.getProductName() + "' successfully");
                    } else {
                        model.addAttribute("products_page_message",
                                "Updated '" + product_update.getProductName() + "' successfully");
                    }
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
            if (admin != null) {
                if (admin.getRoleId().equals("Admin")) {
                    Product pro = proSer.getProductById(productId);
                    pro.setStatus(false);
                    proSer.saveProduct(pro);
                    model.addAttribute("products_page_message",
                            "Set off status product '" + pro.getProductName() + "' successfully");
                    url = listProduct(request, model);
                } else {
                    url = "error-page";
                    model.addAttribute("error_message", "Login with role admin before update product, please!");
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

    @GetMapping("/search")
    public String searchProduct(HttpServletRequest request, Model model,
                                @RequestParam("searchValue") String searchValue) {
        String url = "admin/check-products";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin != null) {
                if (admin.getRoleId().equals("Admin")) {
                    Page<Product> page = proSer.findByProductNameContaining(searchValue, 1, "id", "asc");
                    List<Product> list = page.getContent();
                    if (list.isEmpty()) {
                        model.addAttribute("products_page_message", "Can not find this product!");
                    }
                    model.addAttribute("searchValue", searchValue);
                    model.addAttribute("products", list);
                    model.addAttribute("currentPage", 1);
                    model.addAttribute("totalPages", page.getTotalPages());
                    model.addAttribute("totalItems", page.getTotalElements());
                    model.addAttribute("sortField", "productName");
                    model.addAttribute("sortDir", "asc");
                    model.addAttribute("reverse", "true");
                } else {
                    url = "error-page";
                    model.addAttribute("error_message", "Login with role admin before update product, please!");
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
