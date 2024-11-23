package com.sample.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket_item", schema = "vetautet")
public class TicketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "stock_initial", nullable = false)
    private int stockInitial;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "stock_available", nullable = false)
    private int stockAvailable;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_stock_prepared", nullable = false)
    private Boolean isStockPrepared = false;

    @NotNull
    @Column(name = "price_original", nullable = false)
    private BigDecimal priceOriginal;

    @NotNull
    @Column(name = "price_flash", nullable = false)
    private BigDecimal priceFlash;

    @NotNull
    @Column(name = "sale_start_time", nullable = false)
    private Date saleStartTime;

    @NotNull
    @Column(name = "sale_end_time", nullable = false)
    private Date saleEndTime;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "status", nullable = false)
    private int status;

    @NotNull
    @Column(name = "activity_id", nullable = false)
    private int activityId;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}