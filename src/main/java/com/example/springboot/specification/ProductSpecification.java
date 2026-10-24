package com.example.springboot.specification;

import com.example.springboot.dto.request.product.filter.ProductFilter;
import com.example.springboot.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> withFilter(com.example.springboot.dto.request.product.filter.ProductFilter f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // luôn ẩn hàng đã xóa mềm
            predicates.add(cb.isNull(root.get("deletedAt")));
            if (f.getKeyword() != null && !f.getKeyword().isBlank()) {
                predicates.add(cb.like(root.get("name"), "%" + f.getKeyword() + "%"));
            }
            if (f.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), f.getCategoryId()));
            }
            if (f.getBrandId() != null) {
                predicates.add(cb.equal(root.get("brandId"), f.getBrandId()));
            }
            if (f.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salePrice"), f.getMinPrice()));
            }
            if (f.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salePrice"), f.getMaxPrice()));
            }
            if (Boolean.TRUE.equals(f.getInStock())) {
                predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
