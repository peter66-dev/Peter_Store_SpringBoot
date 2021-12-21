package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.Order;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderService {
    public List<Order> getAllOrders();

    public Order getOrderById(int id);

    public void saveOrder(Order o);

    public List<Order> getOrdersByCustomerName(String name);

    public List<Order> getOrderBetweenDate(Date start, Date end);
}
