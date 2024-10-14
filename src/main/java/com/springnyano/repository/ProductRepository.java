package com.springnyano.repository;

import com.springnyano.entity.ProductEntity;

import java.util.List;

public interface ProductRepository {

    public ProductEntity createProduct(ProductEntity product);

    public List<ProductEntity> findAllProducts();
}
