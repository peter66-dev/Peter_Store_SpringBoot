package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.Product;

import java.util.List;

public interface ProductService {

    public List<Product> getAllProducts();

    public List<Product> findByStatusTrue();

    public List<Product> findByProductNameContaining(String productName);

    public Product getProductById(int id);

    public void saveProduct(Product pro);

    public void deleteProduct(int id);

    public void setStatusProductQuantity();

}
