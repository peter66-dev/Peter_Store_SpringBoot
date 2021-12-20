package com.peter.springboot.store.controller;

import com.peter.springboot.store.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class ProductCRUDController {

    private ProductService proSer;

    public ProductCRUDController(ProductService proSer) {
        this.proSer = proSer;
    }


}
