package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {//provide @Transactional
    public List<Customer> findByStatusTrue();
    public List<Customer> findByNameContaining(String searchValue);
}
