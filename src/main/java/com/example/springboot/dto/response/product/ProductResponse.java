package com.example.springboot.dto.response.product;

import com.example.springboot.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;
    private String sku;
    private String name;
    private String slug;
    private Long salePrice;
    private Long originalPrice;
    private Integer stockQuantity;
    private Integer soldCount;
    private ProductStatus status;
    private String thumbnailUrl;
}
