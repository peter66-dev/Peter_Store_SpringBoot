package com.peter.springboot.store.service;

import com.peter.springboot.store.dao.ProductRepository;
import com.peter.springboot.store.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Product> getAllProducts() {

        return repo.findAll();
    }

    @Override
    public List<Product> findByStatusTrue() {
        return repo.findByStatusTrue();
    }

    @Override
    public List<Product> findByProductNameContaining(String productName) {
        return repo.findByProductNameContaining(productName);
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
    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    @Override
    public void setStatusProductQuantity() {
        List<Product> list = repo.findAll();
        for (Product p: list) {
            if(p.getQuantityInStock() <= 0){
                p.setStatus(false);
                repo.save(p);
            }
        }
    }
}
