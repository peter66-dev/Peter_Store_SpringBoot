package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {//provide @Transactional
}
