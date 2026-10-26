package com.example.springboot.dto.request.brand.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandCreateRequest {
    @NotBlank(message = "Tên thương hiệu không được  trống")
    private String name;

    private String logoUrl;
}
