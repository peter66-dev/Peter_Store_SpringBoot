package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    public List<OrderDetail> getAllOrderDetail();

    public List<OrderDetail> getAllOrderDetailByOrderId(int id);

    public String saveOrderDetail(OrderDetail od);
}
