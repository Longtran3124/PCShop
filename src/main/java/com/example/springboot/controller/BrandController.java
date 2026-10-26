package com.example.springboot.controller;

import com.example.springboot.dto.request.brand.create.BrandCreateRequest;
import com.example.springboot.dto.request.brand.update.BrandUpdateRequest;
import com.example.springboot.dto.response.brand.BrandResponse;
import com.example.springboot.service.BrandService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

    BrandService brandService;

    // Chi tiết 1 thương hiệu theo id (đã lọc hàng xóa mềm).
    // GET http://localhost:8111/api/brands/1
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getById(id));
    }

    // Chi tiết theo slug
    // GET http://localhost:8111/api/brands/slug/asus
    @GetMapping("/slug/{slug}")
    public ResponseEntity<BrandResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(brandService.getBySlug(slug));
    }

    // Tìm thương hiệu theo tên
    // GET http://localhost:8111/api/brands/search?keyword=asus
    @GetMapping("/search")
    public ResponseEntity<List<BrandResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(brandService.findByNameContaining(keyword));
    }

    // Danh sách cho KHÁCH: chỉ thương hiệu còn tồn tại (deletedAt = null).
    // GET http://localhost:8111/api/brands
    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllActive() {
        return ResponseEntity.ok(brandService.findAllActive());
    }

    // Danh sách cho ADMIN: tất cả, kể cả hàng đã xóa mềm.
    // GET http://localhost:8111/api/brands/all
    @GetMapping("/all")
    public ResponseEntity<List<BrandResponse>> getAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    // Thùng rác: thương hiệu đã xóa mềm (admin).
    // GET http://localhost:8111/api/brands/trash
    @GetMapping("/trash")
    public ResponseEntity<List<BrandResponse>> getTrash() {
        return ResponseEntity.ok(brandService.findTrash());
    }

    // Tạo thương hiệu mới. Body JSON, thành công trả 201 Created.
    // POST http://localhost:8111/api/brands
    @PostMapping
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.create(request));
    }

    // Cập nhật: chỉ sửa field client gửi lên, field bỏ trống giữ nguyên.
    // PUT http://localhost:8111/api/brands/1
    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BrandUpdateRequest request) {
        return ResponseEntity.ok(brandService.update(id, request));
    }

    // Xóa mềm: đánh dấu deletedAt = now().
    // DELETE http://localhost:8111/api/brands/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        brandService.softDelete(id);
        return ResponseEntity.ok("Đã xóa thương hiệu id = " + id);
    }

    // Khôi phục thương hiệu đã xóa mềm (deletedAt = null).
    // POST http://localhost:8111/api/brands/1/restore
    @PostMapping("/{id}/restore")
    public ResponseEntity<BrandResponse> restore(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.restore(id));
    }
}
