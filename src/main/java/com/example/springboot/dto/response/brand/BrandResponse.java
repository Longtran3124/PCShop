package com.example.springboot.dto.response.brand;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BrandResponse {
    private Long id;
    private String name;
    private String slug;
    private String logoUrl;
}
