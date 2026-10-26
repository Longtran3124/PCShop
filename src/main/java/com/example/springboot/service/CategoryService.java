package com.example.springboot.service;

import com.example.springboot.dto.request.category.create.CategoryCreateRequest;
import com.example.springboot.dto.request.category.update.CategoryUpdateRequest;
import com.example.springboot.dto.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getById(Long id);

    CategoryResponse getBySlug(String slug);

    List<CategoryResponse> findAll(); // admin dùng để lấy tất cả sản phẩm

    List<CategoryResponse> findAllActive(); // khách dùng

    List<CategoryResponse> findByNameContaining(String keyword);

    CategoryResponse create(CategoryCreateRequest request);

    CategoryResponse update(Long id, CategoryUpdateRequest request);

    void softDelete(Long id);

    CategoryResponse restore(Long id);

    List<CategoryResponse> findTrash();
}
