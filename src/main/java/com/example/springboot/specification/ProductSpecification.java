package com.example.springboot.specification;

import com.example.springboot.dto.request.product.filter.ProductFilter;
import com.example.springboot.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    // Dựng điều kiện WHERE ĐỘNG: chỉ thêm điều kiện khi tham số lọc tương ứng có giá trị.
    // Nhờ vậy 1 hàm xử lý được mọi tổ hợp lọc (chỉ giá, giá + brand, cả 4 điều kiện...) mà không phải viết nhiều method findBy.
    // root = bảng products, cb = "công cụ" tạo điều kiện, predicates = danh sách điều kiện sẽ nối bằng AND.
    public static Specification<Product> withFilter(ProductFilter f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Điều kiện LUÔN có: ẩn hàng đã xóa mềm (deletedAt IS NULL)
            predicates.add(cb.isNull(root.get("deletedAt")));

            // Tìm theo tên (LIKE %keyword%) — chỉ thêm khi keyword không rỗng
            if (f.getKeyword() != null && !f.getKeyword().isBlank()) {
                predicates.add(cb.like(root.get("name"), "%" + f.getKeyword() + "%"));
            }
            // Lọc đúng danh mục
            if (f.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), f.getCategoryId()));
            }
            // Lọc đúng thương hiệu
            if (f.getBrandId() != null) {
                predicates.add(cb.equal(root.get("brandId"), f.getBrandId()));
            }
            // Giá bán >= minPrice
            if (f.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salePrice"), f.getMinPrice()));
            }
            // Giá bán <= maxPrice
            if (f.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salePrice"), f.getMaxPrice()));
            }
            // Chỉ hàng còn tồn kho (stockQuantity > 0) khi inStock = true
            if (Boolean.TRUE.equals(f.getInStock())) {
                predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
            }

            // Nối tất cả điều kiện bằng AND -> ra mệnh đề WHERE hoàn chỉnh
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
