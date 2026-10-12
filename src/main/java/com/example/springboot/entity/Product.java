package com.example.springboot.entity;

import com.example.springboot.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity // để thông báo cho java biết đây là 1 entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE) // để khai báo các biến đều ở phạm vi private
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // dùng AUTO_INCREMENT của MySQL
    Long id; // nên để các giá trị ở wrapper vì nó có thể null

    @Column(name = "sku", nullable = false, unique = true, length = 50)
    String sku;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "slug", nullable = false, unique = true, length = 280)
    String slug;

    // khóa ngoại, tạm map dạng id cho đến khi có entity Category, Brand, Series
    @Column(name = "category_id", nullable = false)
    Long categoryId;

    @Column(name = "brand_id", nullable = false)
    Long brandId;

    @Column(name = "series_id")
    Long seriesId;

    // giá lưu dạng số nguyên (VND) nên dùng Long
    @Column(name = "original_price", nullable = false)
    Long originalPrice;

    @Column(name = "sale_price", nullable = false)
    Long salePrice;

    // gán sẵn giá trị mặc định vì Hibernate sẽ insert null nếu để trống
    @Column(name = "stock_quantity", nullable = false)
    Integer stockQuantity = 0;

    @Column(name = "sold_count", nullable = false)
    Integer soldCount = 0;

    @Column(name = "warranty_months")
    Short warrantyMonths; // smallint

    @Column(name = "thumbnail_url", length = 500)
    String thumbnailUrl;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

    @Enumerated(EnumType.STRING) // lưu tên enum ('DRAFT') thay vì số thứ tự
    @Column(name = "status", nullable = false, length = 20)
    ProductStatus status = ProductStatus.DRAFT;

    @Version // optimistic locking, tránh 2 người cùng sửa đè lên nhau
    @Column(name = "version", nullable = false)
    Integer version;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt; // khác null nghĩa là đã xóa mềmcategories

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
