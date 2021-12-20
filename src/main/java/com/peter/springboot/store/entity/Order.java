package com.peter.springboot.store.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "discount")
    private float discount;

    @Column(name = "total")
    private double total;

    @Column(name = "status")
    private boolean status;

    public Order() {
    }

    public Order(Customer c) {
        customer = c;
        orderDate = new Date();
        status = false;
    }

    public Order(int id, Customer customer, Date orderDate, float discount, double total, boolean status) {
        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
        this.discount = discount;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", orderDate=" + orderDate +
                ", discount=" + discount +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}
