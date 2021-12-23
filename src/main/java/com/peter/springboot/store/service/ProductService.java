package com.peter.springboot.store.service;

import com.peter.springboot.store.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<Product> listByPage(int page, String sortField, String sortDir);

    Page<Product> showProductStore(int page, String sortField, String sortDir);

    Page<Product> findByProductNameContaining(String productName, int page, String sortField, String sortDir);

    Product getProductById(int id);

    void saveProduct(Product pro);

    void setStatusProductQuantity();

}
