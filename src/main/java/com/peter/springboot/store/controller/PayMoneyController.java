package com.peter.springboot.store.controller;

import com.peter.springboot.store.entity.*;
import com.peter.springboot.store.service.CustomerService;
import com.peter.springboot.store.service.OrderDetailService;
import com.peter.springboot.store.service.OrderService;
import com.peter.springboot.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/paymoney")
public class PayMoneyController {

    @Autowired
    private final OrderService orSer;

    @Autowired
    private final ProductService proSer;

    @Autowired
    private final CustomerService cusSer;

    @Autowired
    private final OrderDetailService orderDetailSer;

    public PayMoneyController(OrderService ss, ProductService p, OrderDetailService od, CustomerService cusSer) {
        this.orSer = ss;
        this.proSer = p;
        this.orderDetailSer = od;
        this.cusSer = cusSer;
    }

    @GetMapping("/paybill")
    public String paybill(HttpServletRequest request, Model model) {
        String url = "store/list-products";
        try {
            HttpSession session = request.getSession();
            Order order = (Order) session.getAttribute("order");
            Cart cart = (Cart) session.getAttribute("cart");
            if (order != null && order.getCustomer().getRoleId().equals("User")) {
                System.out.println("Checked user role!");
                List<String> checkList = checkQuantity(cart);
                if (checkList.isEmpty()) {
                    Customer customer = order.getCustomer();
                    customer.setPoints(customer.getPoints() + 1);
                    cusSer.saveCustomer(customer);

                    order.setStatus(true);
                    order.setTotal((Math.round((float) order.getTotal() * (1 - order.getDiscount()) * 100.0) / 100.0));
                    orSer.saveOrder(order);
                    System.out.println("Saved order! Order info: " + order);

                    //saving order detail
                    for (Product pro : cart.getCart().values()) {
                        OrderDetail od = new OrderDetail(pro, order, pro.getQuantityInStock(), pro.getQuantityInStock() * pro.getExportPrice());
                        orderDetailSer.saveOrderDetail(od);
                        System.out.println("Saved success! Order detail info: " + od);
                    }

                    // Sub product quantity in stock
                    subQuantityProduct(cart);
                    System.out.println("Subtracted quantity product success!");
                    proSer.setStatusProductQuantity();

                    System.out.println("Deleted cart: " + cart.deleteAll() + " product!");
                    session.removeAttribute("cart");
                    session.removeAttribute("order");
                    session.removeAttribute("total");
                    model.addAttribute("products", proSer.findByStatusTrue());
                    model.addAttribute("message_store", "Thank you for supporting my small business!");
                } else {
                    String msg = "Sorry, we don't have enough quantity for these products ... ";
                    for (String s : checkList) {
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

    public List<String> checkQuantity(Cart cart) {
        List<String> list = new ArrayList<>();
        for (Product pro : cart.getCart().values()) {
            if (pro.getQuantityInStock() > proSer.getProductById(pro.getId()).getQuantityInStock()) { // Không đủ hàng cho sp này!
                list.add(pro.getProductName());
            }
        }
        return list;
    }

    public void subQuantityProduct(Cart cart) {
        for (Product pro : cart.getCart().values()) {
            Product proInStock = proSer.getProductById(pro.getId());
            proInStock.setQuantityInStock(proInStock.getQuantityInStock() - pro.getQuantityInStock());
            proSer.saveProduct(proInStock);
        }
    }
}
