package com.sample.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class TicketDetail {
    private Long id;
    private String name;
    private String description;
    private Integer stockInitial;
    private Integer stockAvailable;
    private Boolean isStockPrepared = false;
    private BigDecimal priceOriginal;
    private BigDecimal priceFlash;
    private Date saleStartTime;
    private Date saleEndTime;
    private Integer status;
    private Integer activityId;
    private Date updatedAt;
    private Date createdAt;


}
