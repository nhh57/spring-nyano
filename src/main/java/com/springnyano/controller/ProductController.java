package com.springnyano.controller;

import com.springnyano.entity.ProductEntity;
import com.springnyano.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/product/add")
    public ProductEntity createProduct(@RequestBody ProductEntity product) {
        return productService.createProduct(product);
    }

    @GetMapping("/products")
    public List<ProductEntity> getAllProducts() {
        return productService.findAllProducts();
    }

}
