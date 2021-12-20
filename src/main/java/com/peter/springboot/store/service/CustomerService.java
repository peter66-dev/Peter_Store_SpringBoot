package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.Customer;

import java.util.List;

public interface CustomerService {

    public List<Customer> getAllCustomers();

    public List<Customer> getAllCustomersActive();

    public List<Customer> findByNameContaining(String searchValue);

    public Customer getCustomer(int id);

    public void saveCustomer(Customer c);

    public void deleteCustomer(int id);

    public Customer checkLogin(String email, String password);
}
