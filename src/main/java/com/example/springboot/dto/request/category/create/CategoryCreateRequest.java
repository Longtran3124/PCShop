package com.example.springboot.dto.request.category.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequest {
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    private Long parentId; // null = danh mục gốc

    @Min(value = 0, message = "Thứ tự hiển thị không được âm")
    private Integer sortOrder; // null -> service set 0

    private Boolean isActive; // null -> service set true
}
