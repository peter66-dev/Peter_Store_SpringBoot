package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    List<Customer> getAllCustomersActive();

    List<Customer> findByNameContaining(String searchValue);

    Customer getCustomer(int id);

    void saveCustomer(Customer c);

    void deleteCustomer(int id);

    Customer checkLogin(String email, String password);
}
