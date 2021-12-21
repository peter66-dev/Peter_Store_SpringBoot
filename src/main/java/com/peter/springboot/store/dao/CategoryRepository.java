package com.peter.springboot.store.dao;

import com.peter.springboot.store.entity.Category;
import com.peter.springboot.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
