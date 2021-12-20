package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/customers")
public class CustomerController {

    private CustomerService ser;

    // 1 constructor nên ko cần dùng @Autowired
    public CustomerController(CustomerService s) {
        ser = s;
    }

    @GetMapping("/list")
    public String getAllCustomers(Model model, HttpServletRequest request) {
        String url = "error-page";
        try {
            HttpSession session = request.getSession();
            Customer userLogin = (Customer) session.getAttribute("userLogin");
            if (userLogin.getRoleId().equals("Admin")) {
                List<Customer> list = ser.getAllCustomers();
                model.addAttribute("customers", list);
                url = "admin/list-customers";
            } else {
                model.addAttribute("error_message", "You are not valid in this page!");
            }
        } catch (Exception ex) {
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/showFormAddCustomer")
    public String showFormAddCustomer(Model model) {
        Customer c = new Customer();
        model.addAttribute("customer", c);
        return "admin/form-customer";
    }

    @PostMapping("/save")
    public String addCustomer(@ModelAttribute("customer") Customer c, Model model) {
        ser.saveCustomer(c);
        model.addAttribute("message_customer", "Updating " + c.getName() + " succesfully!");
        return "redirect:/admin/customers/list";
    }

    @GetMapping("/showFormUpdateCustomer")
    public String showFormUpdate(@RequestParam("customerId") int id, Model model, HttpServletRequest request) {
        String url = "error-page";
        try {
            HttpSession session = request.getSession();
            Customer userLogin = (Customer) session.getAttribute("userLogin");
            if (userLogin.getRoleId().equals("Admin")) {
                Customer c = ser.getCustomer(id);
                url = "admin/form-customer";
                model.addAttribute("customer", c);
            } else {
                model.addAttribute("error_message", "You are not valid in this page!");
            }
        } catch (Exception ex) {
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    @GetMapping("/delete")
    public String deleteCustomer(@RequestParam("customerId") int id, Model model, HttpServletRequest request) {
        String url = "admin/list-customers";
        try {
            HttpSession session = request.getSession();
            Customer userLogin = (Customer) session.getAttribute("userLogin");
            if (userLogin.getRoleId().equals("Admin")) {
                Customer user = ser.getCustomer(id);
                if (user.getRoleId().equals("Admin")) {
                    model.addAttribute("message_customer", "Can not delete admin, please!");
                } else if (userLogin.getId() == user.getId()) {
                    model.addAttribute("message_customer",
                            "You can not delete your account, please!\nDon't kidding me, " + user.getName() + "!");
                } else {
                    ser.deleteCustomer(id);
                    model.addAttribute("message_customer",
                            "Delete customer " + user.getName() + " successfully!");
                }
            }
        } catch (Exception ex) {
            url = "error-page";
            model.addAttribute("error_message", ex.getMessage());
        }
        model.addAttribute("customers", ser.getAllCustomers());
        return url;
    }

}
