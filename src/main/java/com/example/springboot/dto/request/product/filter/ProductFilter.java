package com.example.springboot.dto.request.product.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilter {
    private String keyword; // tìm theo tên
    private Long categoryId;
    private Long brandId;
    private Long minPrice;
    private Long maxPrice;
    private Boolean inStock; // true = chỉ hàng còn tồn kho
}
