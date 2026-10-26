package com.example.springboot.service.imp;

import com.example.springboot.dto.request.category.create.CategoryCreateRequest;
import com.example.springboot.dto.request.category.update.CategoryUpdateRequest;
import com.example.springboot.dto.response.category.CategoryResponse;
import com.example.springboot.entity.Category;
import com.example.springboot.mapper.CategoryMapper;
import com.example.springboot.repository.CategoryRepository;
import com.example.springboot.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImp implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    private Category getEntityById(Long id) {
        return categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));
    }

    @Override
    public CategoryResponse getById(Long id) {
        return CategoryMapper.toResponse(getEntityById(id));
    }

    @Override
    public CategoryResponse getBySlug(String slug) {
        Category c = categoryRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với slug: " + slug));
        return CategoryMapper.toResponse(c);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream().map(CategoryMapper::toResponse).toList();
    }

    @Override
    public List<CategoryResponse> findAllActive() {
        return categoryRepository.findByDeletedAtIsNull()
                .stream().map(CategoryMapper::toResponse).toList();
    }

    @Override
    public List<CategoryResponse> findByNameContaining(String keyword) {
        return categoryRepository.findByNameContainingAndDeletedAtIsNull(keyword)
                .stream().map(CategoryMapper::toResponse).toList();
    }

    @Override
    public CategoryResponse create(CategoryCreateRequest request) {
        Category c = CategoryMapper.toEntity(request);
        c.setSlug(generateSlug(request.getName()));
        if (c.getSortOrder() == null) {
            c.setSortOrder(0);
        }
        if (c.getIsActive() == null) {
            c.setIsActive(true);
        }
        return CategoryMapper.toResponse(categoryRepository.save(c));
    }

    @Override
    public CategoryResponse update(Long id, CategoryUpdateRequest request) {
        Category existing = getEntityById(id);
        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getParentId() != null) {
            existing.setParentId(request.getParentId());
        }
        if (request.getSortOrder() != null) {
            existing.setSortOrder(request.getSortOrder());
        }
        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }
        return CategoryMapper.toResponse(categoryRepository.save(existing));
    }

    @Override
    public void softDelete(Long id) {
        Category existing = getEntityById(id);
        existing.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(existing);
    }

    @Override
    public CategoryResponse restore(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));
        if (c.getDeletedAt() == null) {
            throw new RuntimeException("Danh mục chưa bị xóa, không cần khôi phục");
        }
        c.setDeletedAt(null);
        return CategoryMapper.toResponse(categoryRepository.save(c));
    }

    @Override
    public List<CategoryResponse> findTrash() {
        return categoryRepository.findByDeletedAtIsNotNull()
                .stream().map(CategoryMapper::toResponse).toList();
    }

    private String generateSlug(String name) {
        return name.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
