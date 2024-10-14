package com.springnyano.service;

import com.springnyano.entity.ProductEntity;

import java.util.List;

public interface ProductService {

    public ProductEntity createProduct(ProductEntity product);

    public List<ProductEntity> findAllProducts();
}
