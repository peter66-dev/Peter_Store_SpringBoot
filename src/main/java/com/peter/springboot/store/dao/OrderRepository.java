package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("select o from Order o where o.customer in (select c from Customer c where c.name like %?1% ) " +
            "and o.status = true Order By o.orderDate desc")
    List<Order> getOrdersByCustomerName(String name);

    List<Order> findAllByOrderByOrderDateDesc();

    List<Order> findByStatusTrueOrderByOrderDateDesc();

    List<Order> findByOrderDateBetweenAndStatusTrueOrderByOrderDateDesc(Date start, Date end);
}
