package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.entity.Order;
import com.peter.springboot.store.entity.Product;
import com.peter.springboot.store.service.OrderDetailService;
import com.peter.springboot.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/bills")
public class BillController {

    @Autowired
    private OrderService orSer;

    @Autowired
    private OrderDetailService odSer;

    public BillController(OrderService orSer, OrderDetailService odSer) {
        this.orSer = orSer;
        this.odSer = odSer;
    }

    @GetMapping("/list")
    public String getAllBills(HttpServletRequest request, Model model) {
        String url = "admin/list-bills";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                List<Order> list = orSer.getAllOrders();
                model.addAttribute("bills",list);
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
