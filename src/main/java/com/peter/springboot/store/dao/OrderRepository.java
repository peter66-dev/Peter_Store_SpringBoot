package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("select o from Order o where o.customer in (select c from Customer c where c.name like %?1% ) Order By o.orderDate desc")
    public List<Order> getOrdersByCustomerName(String name);

    public List<Order> findAllByOrderByOrderDateDesc();

    public List<Order> findByOrderDateBetween(Date start, Date end);
}
