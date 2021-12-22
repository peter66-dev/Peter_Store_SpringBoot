package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByProductNameContaining(String productName);

    List<Product> findByStatusTrue();

}
