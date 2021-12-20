package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.Customer;
import com.peter.springboot.store.entity.Order;
import com.peter.springboot.store.entity.OrderDetail;
import com.peter.springboot.store.entity.Product;
import com.peter.springboot.store.service.OrderDetailService;
import com.peter.springboot.store.service.OrderService;
import com.peter.springboot.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/paymoney")
public class PayMoneyController {

    @Autowired
    private OrderService orSer;

    @Autowired
    private ProductService proSer;

    @Autowired
    private OrderDetailService orderDetailSer;

    public PayMoneyController(OrderService ss, ProductService p, OrderDetailService od) {
        orSer = ss;
        proSer = p;
        orderDetailSer = od;
    }

    @GetMapping("/paybill")
    public String paybill(HttpServletRequest request, Model model) {
        String url = "store/list-products";
        try {
            HttpSession session = request.getSession();
            Order order = (Order) session.getAttribute("order");
            ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
            if (order != null && order.getCustomer().getRoleId().equals("User")) {
                System.out.println("Checked user role!");
                if (checkQuantity(cart).isEmpty()) {
                    order.setStatus(true);
                    double discount = Math.random() / 2;
                    order.setDiscount((float)(Math.round((float)discount * 100.0) / 100.0));

                    order.setTotal((Math.round((float)order.getTotal() * (1 - discount) * 100.0) / 100.0));
                    orSer.saveOrder(order);
                    System.out.println("Saved order! Order info: " + order);

                    //saving order detail
                    for (Product pro : cart) {
                        OrderDetail od = new OrderDetail(pro, order, pro.getQuantityInStock(), pro.getQuantityInStock() * pro.getExportPrice());
                        orderDetailSer.saveOrderDetail(od);
                        System.out.println("Saved success! Order detail info: " + od);
                    }

                    // Sub product quantity in stock
                    subQuantityProduct(cart);
                    System.out.println("Subtracted quantity product success!");
                    proSer.setStatusProductQuantity();

                    cart.removeAll(cart);
                    model.addAttribute("products", proSer.getAllProducts());
                    model.addAttribute("message_store","Thank you for supporting my small business!");
                } else {
                    String msg = "Sorry, we don't have enough quantity for these products ... ";
                    for (String s : checkQuantity(cart)) {
                        msg += "\n" + s;
                    }
                    model.addAttribute("message_cart", msg);
                }
            } else {
                System.out.println("!!!!!!!!!!!!!!!!!!! NOT A USER ROLE !!!!!!!!!!!!!!!!!!!");
                model.addAttribute("error_message", "CHỈ CÓ USER ĐƯỢC MUA HÀNG Ở ĐÂY THÔI!");
            }
        } catch (Exception ex) {
            url = "error-page";
            model.addAttribute("error_message", ex.getMessage());
        }
        return url;
    }

    public List<String> checkQuantity(ArrayList<Product> cart) {
        List<String> list = new ArrayList<>();
        for (Product pro : cart) {
            if (pro.getQuantityInStock() > proSer.getProductById(pro.getId()).getQuantityInStock()) { // Không đủ hàng cho sp này!
                list.add(pro.getProductName());
            }
        }
        return list;
    }

    public void subQuantityProduct(ArrayList<Product> cart){
        for (Product pro: cart) {
            Product proInStock = proSer.getProductById(pro.getId());
            proInStock.setQuantityInStock(proInStock.getQuantityInStock() - pro.getQuantityInStock());
            proSer.saveProduct(proInStock);
        }
    }
}
