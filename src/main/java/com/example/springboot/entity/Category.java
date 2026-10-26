package com.example.springboot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "parent_id")
    Long parentId;

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "slug", nullable = false, unique = true, length = 120)
    String slug;

    // Thứ tự hiển thị (sắp menu). Mặc định 0.
    @Column(name = "sort_order", nullable = false)
    Integer sortOrder = 0;

    // Bật/tắt danh mục. tinyint(1) trong MySQL <-> Boolean trong Java.
    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

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
