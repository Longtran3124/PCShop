package com.example.springboot.service;

import com.example.springboot.dto.request.brand.create.BrandCreateRequest;
import com.example.springboot.dto.request.brand.update.BrandUpdateRequest;
import com.example.springboot.dto.response.brand.BrandResponse;

import java.util.List;

public interface BrandService {

    BrandResponse getById(Long id);

    BrandResponse getBySlug(String slug);

    List<BrandResponse> findAll();

    List<BrandResponse> findAllActive();

    List<BrandResponse> findByNameContaining(String keyword);

    BrandResponse create(BrandCreateRequest request);

    BrandResponse update(Long id, BrandUpdateRequest request);

    void softDelete(Long id);

    BrandResponse restore(Long id);

    List<BrandResponse> findTrash();
}
