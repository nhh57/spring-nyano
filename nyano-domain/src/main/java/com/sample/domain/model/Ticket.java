package com.sample.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class Ticket {
    private Long id;
    private String name;
    private String desc;
    private Date startTime;
    private Date endTime;
    private Integer status;
    private Date updatedAt;
    private Date createdAt;

}