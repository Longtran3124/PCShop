package com.example.springboot.service.imp;

import com.example.springboot.dto.request.brand.create.BrandCreateRequest;
import com.example.springboot.dto.request.brand.update.BrandUpdateRequest;
import com.example.springboot.dto.response.brand.BrandResponse;
import com.example.springboot.entity.Brand;
import com.example.springboot.mapper.BrandMapper;
import com.example.springboot.repository.BrandRepository;
import com.example.springboot.service.BrandService;
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
public class BrandServiceImp implements BrandService {

    @Autowired
    BrandRepository brandRepository;

    private Brand getEntityById(Long id) {
        return brandRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu với id: " + id));
    }

    @Override
    public BrandResponse getById(Long id) {
        return BrandMapper.toResponse(getEntityById(id));
    }

    @Override
    public BrandResponse getBySlug(String slug) {
        Brand b = brandRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu với slug: " + slug));
        return BrandMapper.toResponse(b);
    }

    @Override
    public List<BrandResponse> findAll() {
        return brandRepository.findAll()
                .stream().map(BrandMapper::toResponse).toList();
    }

    @Override
    public List<BrandResponse> findAllActive() {
        return brandRepository.findByDeletedAtIsNull()
                .stream().map(BrandMapper::toResponse).toList();
    }

    @Override
    public List<BrandResponse> findByNameContaining(String keyword) {
        return brandRepository.findByNameContainingAndDeletedAtIsNull(keyword)
                .stream().map(BrandMapper::toResponse).toList();
    }

    @Override
    public BrandResponse create(BrandCreateRequest request) {
        Brand b = BrandMapper.toEntity(request);
        b.setSlug(generateSlug(request.getName()));
        return BrandMapper.toResponse(brandRepository.save(b));
    }

    @Override
    public BrandResponse update(Long id, BrandUpdateRequest request) {
        Brand existing = getEntityById(id);
        if (request.getName() != null)    existing.setName(request.getName());
        if (request.getLogoUrl() != null) existing.setLogoUrl(request.getLogoUrl());
        return BrandMapper.toResponse(brandRepository.save(existing));
    }

    @Override
    public void softDelete(Long id) {
        Brand existing = getEntityById(id);
        existing.setDeletedAt(LocalDateTime.now());
        brandRepository.save(existing);
    }

    @Override
    public BrandResponse restore(Long id) {
        Brand b = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu với id: " + id));
        if (b.getDeletedAt() == null) {
            throw new RuntimeException("Thương hiệu chưa bị xóa, không cần khôi phục");
        }
        b.setDeletedAt(null);
        return BrandMapper.toResponse(brandRepository.save(b));
    }

    @Override
    public List<BrandResponse> findTrash() {
        return brandRepository.findByDeletedAtIsNotNull()
                .stream().map(BrandMapper::toResponse).toList();
    }

    // biến tên thương hiệu lúc mới tạo thành slug, rồi lưu nó va db
    private String generateSlug(String name) {
        return name.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
