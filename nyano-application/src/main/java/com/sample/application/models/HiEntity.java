package com.sample.application.models;


import lombok.Data;

import java.util.UUID;

@Data
public class HiEntity {

    private Long id;

    private UUID uuid;

    private String name;


}
