package com.springnyano.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@ToString
@Builder
@Table(name = "java_user_001")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(255) comment 'user name'", nullable = false)
    private String userName;
    @Column(columnDefinition = "varchar(255) comment 'user email'", unique = true, nullable = true)
    private String userEmail;

}
