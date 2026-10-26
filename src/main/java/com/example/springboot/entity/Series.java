package com.example.springboot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "series")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Series implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "brand_id", nullable = false)   // bắt buộc thuộc 1 brand
    Long brandId;

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "slug", nullable = false, length = 120)   // KHÔNG unique
    String slug;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    Long createdBy;

    @Column(name = "updated_by")
    Long updatedBy;
}
