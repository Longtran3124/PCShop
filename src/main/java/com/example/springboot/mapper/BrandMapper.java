package com.example.springboot.mapper;

import com.example.springboot.dto.request.brand.create.BrandCreateRequest;
import com.example.springboot.dto.response.brand.BrandResponse;
import com.example.springboot.entity.Brand;

public class BrandMapper {
    public static Brand toEntity(BrandCreateRequest req) {
        Brand b = new Brand();
        b.setName(req.getName());
        b.setLogoUrl(req.getLogoUrl());
        return b;   // slug do service sinh
    }
    public static BrandResponse toResponse(Brand b) {
        return BrandResponse.builder()
                .id(b.getId())
                .name(b.getName())
                .slug(b.getSlug())
                .logoUrl(b.getLogoUrl())
                .build();
    }
}
