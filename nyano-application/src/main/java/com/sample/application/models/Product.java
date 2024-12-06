package com.sample.application.models;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    private Long id;
    private String name;
    private int categoryId;
    private String description;
    private BigDecimal price;
}
