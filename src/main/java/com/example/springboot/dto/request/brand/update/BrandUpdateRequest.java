package com.example.springboot.dto.request.brand.update;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandUpdateRequest {
    private String name; // null = giữ nguyên
    private String logoUrl; // null = giữ nguyên
}
