package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    List<OrderDetail> findOrderDetailByOrderId(int id);

}
