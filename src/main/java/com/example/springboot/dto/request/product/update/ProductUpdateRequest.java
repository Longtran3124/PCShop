package com.example.springboot.dto.request.product.update;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductUpdateRequest {
    private String name;

    @Min(value = 0, message = "Giá bán không được âm")
    private Long salePrice;

    @Min(value = 0, message = "Tồn kho không được âm")
    private Integer stockQuantity;
}
