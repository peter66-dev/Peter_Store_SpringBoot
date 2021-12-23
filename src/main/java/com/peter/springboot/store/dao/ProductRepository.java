package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query("select p from Product p where p.productName like %?1%")
    Page<Product> findByProductNameContaining(String productName, Pageable page);

    List<Product> findByStatusTrue();

    @Query("select p from Product p where p.productName like %?1% and p.status = true")
    Page<Product> showProductStore(String productName, Pageable page);

    @Transactional
    @Modifying
    @Query("update Product p set p.status = false where p.quantityInStock = 0")
    void setStatusProductQuantity();


}
