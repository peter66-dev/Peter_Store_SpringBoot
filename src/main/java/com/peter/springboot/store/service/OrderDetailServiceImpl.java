package com.peter.springboot.store.service;

import com.peter.springboot.store.dao.OrderDetailRepository;
import com.peter.springboot.store.entity.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private OrderDetailRepository repo;

    public OrderDetailServiceImpl(OrderDetailRepository r) {
        repo = r;
    }

    @Override
    public List<OrderDetail> getAllOrderDetail() {
        return repo.findAll();
    }

    @Override
    public List<OrderDetail> getAllOrderDetailByOrderId(int id) {
        return repo.findOrderDetailByOrderId(id);
    }

    @Override
    public String saveOrderDetail(OrderDetail od) {
        repo.save(od);
        return "Saving successfully! Order detail: " + od;
    }

    @Override
    public OrderDetail getOrderDetailById(int id) {
        Optional<OrderDetail> o = repo.findById(id);
        OrderDetail od = null;
        if(o.isPresent()){
            od = o.get();
        }
        return od;
    }
}
