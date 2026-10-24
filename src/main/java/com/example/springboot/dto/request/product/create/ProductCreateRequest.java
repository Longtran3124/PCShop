package com.example.springboot.dto.request.product.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {
    @NotBlank(message = "SKU không được để trống")
    private String sku;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Phải có danh mục")
    private Long categoryId;

    @NotNull(message = "Phải có thương hiệu")
    private Long brandId;
    private Long seriesId;

    @NotNull @Min(value = 0, message = "giá gốc phải >0")
    private Long originalPrice;

    @NotNull @Min(value = 0, message = "giá bán phải >= 0")
    private Long salePrice;

    @Min(value = 0, message = "Tồn kho không được âm")
    private Integer stockQuantity;

    @Min(0)
    private Short warrantyMonths;

    private String thumbnailUrl;

    private String description;
}
