package com.example.springboot.dto.request.series.update;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeriesUpdateRequest {

    private String name;     // null = giữ nguyên
    private Long brandId;    // null = giữ nguyên
}
