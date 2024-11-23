package com.sample.infrastructure.persistence.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.Date;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket", schema = "vetautet")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Lob
    @Column(name = "`desc`")
    private String desc;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "status", nullable = false)
    private int status;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}