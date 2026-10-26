package com.example.springboot.dto.request.series.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeriesCreateRequest {

    @NotNull(message = "Phải chọn thương hiệu")
    private Long brandId;

    @NotBlank(message = "Tên series không được để trống")
    private String name;
}
