package com.peter.springboot.store.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.REMOVE)
    List<OrderDetail> orderDetails;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
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

    public void setDiscount() {
        float result = 0f;
        float target = 0f;
        int pointsCustomer = this.customer.getPoints();
        while (true) {
            result = (float) Math.random();
            if (pointsCustomer >= 0) {
                target = 0.1f;
            } else if (pointsCustomer >= 20) {
                target = 0.2f;
            } else if (pointsCustomer >= 40) {
                target = 0.3f;
            } else if (pointsCustomer >= 60) {
                target = 0.4f;
            } else if (pointsCustomer >= 80 && pointsCustomer < 100) {
                target = 0.5f;
            } else {
                target = 0.6f;
            }
            if (result <= target) {
                this.discount = (float) (Math.round(result * 100.0) / 100.0);
                break;
            }
        }
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
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
