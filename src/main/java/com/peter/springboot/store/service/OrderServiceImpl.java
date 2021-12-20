package com.peter.springboot.store.service;

import com.peter.springboot.store.dao.OrderRepository;
import com.peter.springboot.store.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    private OrderRepository repo;

    public OrderServiceImpl(OrderRepository r){
        repo = r;
    }

    @Override
    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    @Override
    public Order getOrderById(int id) {
        Optional<Order> o = repo.findById(id);
        Order ord = null;
        if(o.isPresent()){
            ord = o.get();
        }
        return ord;
    }

    @Override
    public void saveOrder(Order o) {
        repo.save(o);
    }
}
