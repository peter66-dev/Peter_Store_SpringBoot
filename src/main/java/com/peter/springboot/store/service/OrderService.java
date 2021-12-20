package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.Order;

import java.util.List;

public interface OrderService {
    public List<Order> getAllOrders();

    public Order getOrderById(int id);

    public void saveOrder(Order o);
    
}
