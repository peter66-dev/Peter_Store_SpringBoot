package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    List<Product> findByStatusTrue();

    List<Product> findByProductNameContaining(String productName);

    Product getProductById(int id);

    void saveProduct(Product pro);

    void deleteProduct(int id);

    void setStatusProductQuantity();

}
