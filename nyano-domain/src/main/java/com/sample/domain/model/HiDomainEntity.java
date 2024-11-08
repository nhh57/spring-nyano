package com.sample.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class HiDomainEntity {

    private Long id;

    private UUID uuid;

    private String name;


}


