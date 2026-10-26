package com.example.springboot.service;

import com.example.springboot.dto.request.series.create.SeriesCreateRequest;
import com.example.springboot.dto.request.series.update.SeriesUpdateRequest;
import com.example.springboot.dto.response.series.SeriesResponse;

import java.util.List;

public interface SeriesService {
    SeriesResponse getById(Long id);

    List<SeriesResponse> findAll();

    List<SeriesResponse> findByNameContaining(String keyword);

    List<SeriesResponse> findByBrandId(Long brandId);   // series của 1 hãng

    SeriesResponse create(SeriesCreateRequest request);

    SeriesResponse update(Long id, SeriesUpdateRequest request);

    void delete(Long id);   // XÓA CỨNG (không có trash/restore)
}
