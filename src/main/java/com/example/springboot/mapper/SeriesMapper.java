package com.example.springboot.mapper;

import com.example.springboot.dto.request.series.create.SeriesCreateRequest;
import com.example.springboot.dto.response.series.SeriesResponse;
import com.example.springboot.entity.Series;

public class SeriesMapper {
    public static Series toEntity(SeriesCreateRequest req) {
        Series s = new Series();
        s.setBrandId(req.getBrandId());
        s.setName(req.getName());
        return s;   // slug do service sinh
    }
    public static SeriesResponse toResponse(Series s) {
        return SeriesResponse.builder()
                .id(s.getId())
                .brandId(s.getBrandId())
                .name(s.getName())
                .slug(s.getSlug())
                .build();
    }
}
