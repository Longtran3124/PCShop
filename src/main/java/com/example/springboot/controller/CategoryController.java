package com.example.springboot.controller;

import com.example.springboot.dto.request.category.create.CategoryCreateRequest;
import com.example.springboot.dto.request.category.update.CategoryUpdateRequest;
import com.example.springboot.dto.response.category.CategoryResponse;
import com.example.springboot.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    // Chi tiết 1 danh mục theo id
    // Get http://localhost:8111/api/categories/1
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    // chi tiết theo slug
    // GET http://localhost:8111/api/categories/slug/laptop-gaming
    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getBySlug(slug));
    }

    // Tìm sản phẩm theo tên
    // GET http://localhost:8111/api/categories/search?keyword=laptop
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(categoryService.findByNameContaining(keyword));
    }

    // Lấy danh sách cho khách
    // GET http://localhost:8111/api/categories
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllActive() {
        return ResponseEntity.ok(categoryService.findAllActive());
    }

    // Lấy danh sách cho admin
    // GET http://localhost:8111/api/categories/all
    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // xem danh mục category đã xóa mềm, chỉ admin mới được xem
    // GET http://localhost:8111/api/categories/trash
    @GetMapping("/trash")
    public ResponseEntity<List<CategoryResponse>> getTrash() {
        return ResponseEntity.ok(categoryService.findTrash());
    }

    // Tạo mới
    // POST http://localhost:8111/api/categories
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }

    // Cập nhật
    // PUT http://localhost:8111/api/categories/1
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    // xóa mềm -> chỉ có admin mới xem được danh sách xóa mềm
    // DELETE http://localhost:8111/api/categories/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        categoryService.softDelete(id);
        return ResponseEntity.ok("Đã xóa danh mục id = " + id);
    }

    // khôi phục xóa mềm
    // POST http://localhost:8111/api/categories/1/restore
    @PostMapping("/{id}/restore")
    public ResponseEntity<CategoryResponse> restore(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.restore(id));
    }
}
