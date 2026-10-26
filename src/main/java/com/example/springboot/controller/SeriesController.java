package com.example.springboot.controller;

import com.example.springboot.dto.request.series.create.SeriesCreateRequest;
import com.example.springboot.dto.request.series.update.SeriesUpdateRequest;
import com.example.springboot.dto.response.series.SeriesResponse;
import com.example.springboot.service.SeriesService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeriesController {
    SeriesService seriesService;
    // Chi tiết 1 series. GET http://localhost:8111/api/series/1
    @GetMapping("/{id}")
    public ResponseEntity<SeriesResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(seriesService.getById(id));
    }
    // Tìm theo tên. GET http://localhost:8111/api/series/search?keyword=rog
    @GetMapping("/search")
    public ResponseEntity<List<SeriesResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(seriesService.findByNameContaining(keyword));
    }
    // Các series của 1 thương hiệu. GET http://localhost:8111/api/series/brand/1
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<SeriesResponse>> getByBrand(@PathVariable Long brandId) {
        return ResponseEntity.ok(seriesService.findByBrandId(brandId));
    }
    // Tất cả series. GET http://localhost:8111/api/series
    @GetMapping
    public ResponseEntity<List<SeriesResponse>> getAll() {
        return ResponseEntity.ok(seriesService.findAll());
    }
    // Tạo mới. POST http://localhost:8111/api/series
    @PostMapping
    public ResponseEntity<SeriesResponse> create(@Valid @RequestBody SeriesCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seriesService.create(request));
    }
    // Cập nhật. PUT http://localhost:8111/api/series/1
    @PutMapping("/{id}")
    public ResponseEntity<SeriesResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SeriesUpdateRequest request) {
        return ResponseEntity.ok(seriesService.update(id, request));
    }
    // XÓA CỨNG. DELETE http://localhost:8111/api/series/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        seriesService.delete(id);
        return ResponseEntity.ok("Đã xóa series id = " + id);
    }
}
