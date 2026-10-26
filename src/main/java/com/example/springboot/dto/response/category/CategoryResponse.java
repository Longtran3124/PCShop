package com.example.springboot.dto.response.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryResponse {
    private Long id;
    private Long parentId;
    private String name;
    private String slug;
    private Integer sortOrder;
    private Boolean isActive;
}
