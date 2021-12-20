package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public List<Product> findByProductNameContaining(String productName);

    public List<Product> findByStatusTrue();

}
