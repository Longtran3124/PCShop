package com.example.springboot.mapper;

import com.example.springboot.dto.request.category.create.CategoryCreateRequest;
import com.example.springboot.dto.response.category.CategoryResponse;
import com.example.springboot.entity.Category;

public class CategoryMapper {
    // Request (tạo mới) -> Entity: chép field từ DTO sang Category để lưu DB
    public static Category toEntity(CategoryCreateRequest req) {
        Category c = new Category();
        c.setName(req.getName());
        c.setParentId(req.getParentId());
        c.setSortOrder(req.getSortOrder());
        c.setIsActive(req.getIsActive());
        return c;
    }

    // Entity -> Response: chép field từ Category sang DTO để trả ra (giấu field nội bộ)
    public static CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .parentId(c.getParentId())
                .name(c.getName())
                .slug(c.getSlug())
                .sortOrder(c.getSortOrder())
                .isActive(c.getIsActive())
                .build();
    }
}
