package com.peter.springboot.store.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "age")
    private int age;

    @Column(name = "password")
    private String password;

    @Column(name = "points")
    private int points;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "status")
    private boolean status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer",cascade = CascadeType.REFRESH)
    private List<Order> orders;

    public Customer() {
    }

    public Customer(String name, String address, String email, boolean gender, int age, String password, int points, String roleId, boolean status) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.password = password;
        this.points = points;
        this.roleId = roleId;
        this.status = status;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", points=" + points +
                ", roleId='" + roleId + '\'' +
                ", status=" + status +
                '}';
    }
}
