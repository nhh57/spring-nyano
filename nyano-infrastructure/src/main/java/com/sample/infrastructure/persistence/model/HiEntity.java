package com.sample.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.util.UUID;

@Entity
@Table(name = "hi_java_001")
@Data
public class HiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private String name;
}
