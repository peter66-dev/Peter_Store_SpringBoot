package com.peter.springboot.store.service;

import com.peter.springboot.store.dao.ProductRepository;
import com.peter.springboot.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    public static final int PRODUCTS_PER_PAGE = 10;

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Page<Product> showProductStore(int page, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page - 1, PRODUCTS_PER_PAGE, sort);
        return repo.showProductStore("_", pageable);
    }

    @Override
    public Page<Product> findByProductNameContaining(String productName, int page, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page - 1, PRODUCTS_PER_PAGE, sort);
        return repo.findByProductNameContaining(productName, pageable);
    }

    @Override
    public Product getProductById(int id) {
        Product p = null;
        Optional<Product> o = repo.findById(id);
        if (o.isPresent()) {
            p = o.get();
        }
        return p;
    }

    @Override
    public void saveProduct(Product pro) {
        repo.save(pro);
    }

    @Override
    public void setStatusProductQuantity() {
        repo.setStatusProductQuantity();
    }

    @Override
    public Page<Product> listByPage(int page, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page - 1, PRODUCTS_PER_PAGE, sort);
        return repo.findAll(pageable);
    }
}
