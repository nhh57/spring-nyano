package com.springnyano.repository.impl;

import com.springnyano.entity.ProductEntity;
import com.springnyano.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public ProductEntity createProduct(ProductEntity product) {
        return ProductEntity.builder()
                .id(1L)
                .productName("TIPS JAVA")
                .productPrice(new BigDecimal(21.6))
                .build();
    }

    @Override
    public List<ProductEntity> findAllProducts() {
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .productName("TIPS JAVA")
                .productPrice(new BigDecimal(21.6))
                .build();
        return List.of(productEntity);
    }
}
