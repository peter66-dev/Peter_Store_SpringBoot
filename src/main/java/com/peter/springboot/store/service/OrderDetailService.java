package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetail> getAllOrderDetail();

    List<OrderDetail> getAllOrderDetailByOrderId(int id);

    String saveOrderDetail(OrderDetail od);

    OrderDetail getOrderDetailById(int id);
}
