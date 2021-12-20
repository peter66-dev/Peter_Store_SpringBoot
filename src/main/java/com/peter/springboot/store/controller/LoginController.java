package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.entity.Product;
import com.peter.springboot.store.service.CustomerService;
import com.peter.springboot.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private CustomerService cusSer;

    @Autowired
    private ProductService proSer;

    public LoginController(CustomerService c, ProductService p) {
        cusSer = c;
        proSer = p;
    }

    @GetMapping("/mainpage")
    public String showMainPage() {
        return "/admin/admin-page";
    }

    @GetMapping("/login")
    public String showFormLogin(Model model) {
        Customer c = new Customer();
        model.addAttribute("loginCustomer", c);
        return "login";
    }

    @PostMapping("/processLogin")
    public String processLogin(@ModelAttribute("loginCustomer") Customer loginCus, Model model,
                               HttpServletRequest request) {
        String url = "/login";
        Customer c = cusSer.checkLogin(loginCus.getEmail(), loginCus.getPassword());
        if (c != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userLogin", c);
            if(c.isStatus()){
                if (c.getRoleId().equals("Admin")) {
                    url = "admin/admin-page";
                } else if(c.getRoleId().equals("User")) { // User role
                    List<Product> list = proSer.findByStatusTrue();
                    url = "store/list-products";
                    model.addAttribute("products", list);
                }else{
                    url = "error-page";
                    model.addAttribute("error_message","Your role is not supported! Create a new account please!");
                }
            }else{
                model.addAttribute("LOGIN_MESSAGE",
                        "Your account is blocked, contact with admin to give more information. Email admin: vanphuong0606@gmail.com");
            }
        } else {
            String msg = "Email or password is not valid!" +
                    " Please try again!";
            model.addAttribute("LOGIN_MESSAGE", msg);
        }
        return url;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/login";
    }

}
