package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.entity.Order;
import com.peter.springboot.store.entity.OrderDetail;
import com.peter.springboot.store.service.OrderDetailService;
import com.peter.springboot.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/bills")
public class BillController {

    @Autowired
    private final OrderService orSer;

    @Autowired
    private final OrderDetailService odSer;

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
                List<Order> list = orSer.findByStatusTrueOrderByOrderDateDesc();
                model.addAttribute("bills", list);
                model.addAttribute("importPrice", (float) (Math.round((float) getImportPriceByOrderList(list) * 100.0) / 100.0));
                model.addAttribute("exportPrice", (float) (Math.round((float) getExportPriceByOrderList(list) * 100.0) / 100.0));
                model.addAttribute("total", (float) (Math.round((float) getTotalByOrders(list) * 100.0) / 100.0));
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
    public String getOrderByName(@RequestParam("searchValue") String searchValue, HttpServletRequest request, Model model) {
        String url = "admin/list-bills";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                List<Order> list = orSer.getOrdersByCustomerName(searchValue);
                model.addAttribute("bills", list);
                model.addAttribute("bills", list);
                model.addAttribute("importPrice", (float) (Math.round((float) getImportPriceByOrderList(list) * 100.0) / 100.0));
                model.addAttribute("exportPrice", (float) (Math.round((float) getExportPriceByOrderList(list) * 100.0) / 100.0));
                model.addAttribute("total", (float) (Math.round((float) getTotalByOrders(list) * 100.0) / 100.0));
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

    @GetMapping("/showOrderDetails")
    public String showFormOrderDetails(@RequestParam("orderId") int orderId, HttpServletRequest request, Model model) {
        String url = "admin/list-bills-details";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                List<OrderDetail> list = odSer.getAllOrderDetailByOrderId(orderId);
                model.addAttribute("bill_details", list);
                model.addAttribute("importPrice", (float) (Math.round((float) getImportPriceByOrderDetailList(list) * 100.0) / 100.0));
                model.addAttribute("exportPrice", (float) (Math.round((float) getExportPriceByOrderDetailList(list) * 100.0) / 100.0));
                model.addAttribute("totalBill", (float) (Math.round((float) orSer.getOrderById(orderId).getTotal() * 100.0) / 100.0));
                model.addAttribute("discount", (float) (Math.round(orSer.getOrderById(orderId).getDiscount() * 100.0) / 100.0));
                model.addAttribute("customerName", orSer.getOrderById(orderId).getCustomer().getName());
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

    @GetMapping("/statistic")
//    public String statistic(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
//                            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
//                            HttpServletRequest request, Model model) {
    public String statistic(@RequestParam("start") String start, @RequestParam("end") String end,
                            HttpServletRequest request, Model model) {
        String url = "admin/list-bills";
        try {
            HttpSession session = request.getSession();
            Customer admin = (Customer) session.getAttribute("userLogin");
            if (admin.getRoleId().equals("Admin")) {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
                Date dStart = f.parse(start + " 00:00:00");
                Date dEnd = f.parse(end + " 23:59:59");
                if (start.compareTo(end) > 0) { // start > end
                    model.addAttribute("message_bill", "Sorry, time is not valid to statistic!");
                } else {
                    List<Order> list = orSer.getOrderBetweenDate(dStart, dEnd);
                    model.addAttribute("bills", list);
                    model.addAttribute("importPrice", (float) (Math.round((float) getImportPriceByOrderList(list) * 100.0) / 100.0));
                    model.addAttribute("exportPrice", (float) (Math.round((float) getExportPriceByOrderList(list) * 100.0) / 100.0));
                    model.addAttribute("total", (float) (Math.round((float) getTotalByOrders(list) * 100.0) / 100.0));
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

    public double getImportPriceByOrderDetailList(List<OrderDetail> orderDetails) {
        double result = 0;
        for (OrderDetail od : orderDetails) {
            result += od.getQuantityBuy() * od.getProduct().getImportPrice();
        }
        return result;
    }

    public double getImportPriceByOrderList(List<Order> list) {
        double result = 0;
        for (Order order : list) {
            for (OrderDetail od : order.getOrderDetails()) {
                result += od.getQuantityBuy() * od.getProduct().getImportPrice();
            }
        }
        return result;
    }

    public double getExportPriceByOrderDetailList(List<OrderDetail> orderDetails) {
        double result = 0;
        for (OrderDetail od : orderDetails) {
            result += od.getQuantityBuy() * od.getProduct().getExportPrice();
        }
        return result;
    }

    public double getExportPriceByOrderList(List<Order> list) {
        double result = 0;
        for (Order order : list) {
            for (OrderDetail od : order.getOrderDetails()) {
                result += od.getQuantityBuy() * od.getProduct().getExportPrice();
            }
        }
        return result;
    }

    public double getTotalByOrders(List<Order> list) {
        double result = 0;
        for (Order order : list) {
            result += order.getTotal();
        }
        return result;
    }

}
