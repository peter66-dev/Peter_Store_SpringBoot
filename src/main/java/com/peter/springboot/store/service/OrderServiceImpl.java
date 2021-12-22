package com.peter.springboot.store.service;

import com.peter.springboot.store.dao.OrderRepository;
import com.peter.springboot.store.entity.Order;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repo;

    public OrderServiceImpl(OrderRepository r) {
        repo = r;
    }

    @Override
    public List<Order> getAllOrders() {
        return repo.findAllByOrderByOrderDateDesc();
    }

    @Override
    public Order getOrderById(int id) {
        Optional<Order> o = repo.findById(id);
        Order ord = null;
        if (o.isPresent()) {
            ord = o.get();
        }
        return ord;
    }

    @Override
    public void saveOrder(Order o) {
        repo.save(o);
    }

    @Override
    public List<Order> getOrdersByCustomerName(String name) {
        return repo.getOrdersByCustomerName(name);
    }

    @Override
    public List<Order> getOrderBetweenDate(Date start, Date end) {
        return repo.findByOrderDateBetweenAndStatusTrueOrderByOrderDateDesc(start, end);
    }

    @Override
    public List<Order> findByStatusTrueOrderByOrderDateDesc() {
        return repo.findByStatusTrueOrderByOrderDateDesc();
    }
}
