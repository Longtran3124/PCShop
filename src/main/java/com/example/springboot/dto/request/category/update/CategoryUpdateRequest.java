package com.example.springboot.dto.request.category.update;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRequest {

    private String name;         // null = giữ nguyên

    private Long parentId;

    @Min(value = 0, message = "Thứ tự hiển thị không được âm")
    private Integer sortOrder;

    private Boolean isActive;
}
