package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.Order;

import java.util.Date;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(int id);

    void saveOrder(Order o);

    List<Order> getOrdersByCustomerName(String name);

    List<Order> getOrderBetweenDate(Date start, Date end);

    List<Order> findByStatusTrueOrderByOrderDateDesc();
}
