package com.example.springboot.controller;

import com.example.springboot.dto.request.product.create.ProductCreateRequest;
import com.example.springboot.dto.response.product.ProductResponse;
import com.example.springboot.entity.Product;
import com.example.springboot.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // được phép các api
@RequestMapping("/api/products")
@RequiredArgsConstructor // tự động sinh ra một constructor chứa các field final (và field @NonNull)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    // Lấy chi tiết 1 sản phẩm theo id (đã lọc hàng xóa mềm). Không thấy -> báo lỗi.
    // GET http://localhost:8111/api/products/1
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    // Tìm sản phẩm theo tên (keyword nằm trên query string ?keyword=...), đã ẩn hàng xóa mềm.
    // GET http://localhost:8111/api/products/search?keyword=iphone
    @GetMapping("/search")
    public ResponseEntity<List<Product>> findByNameContaining(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.findByNameContaining(keyword));
    }

    // Tạo sản phẩm mới. Dữ liệu nằm trong body (JSON). Thành công trả 201 Created kèm sản phẩm vừa tạo.
    // POST http://localhost:8111/api/products
    @PostMapping
    public ResponseEntity<ProductResponse> create( @Valid @RequestBody ProductCreateRequest request) {
        ProductResponse created = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Cập nhật sản phẩm: @PathVariable lấy id từ URL, @RequestBody lấy dữ liệu mới từ body.
    // Chỉ sửa field client gửi lên, field bỏ trống giữ nguyên giá trị cũ.
    // PUT http://localhost:8111/api/products/1
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.update(id, product);
        return ResponseEntity.ok(updated);
    }

    // Xóa mềm: đánh dấu deletedAt = now(), sản phẩm bị ẩn khỏi web nhưng dữ liệu vẫn còn trong DB.
    // DELETE http://localhost:8111/api/products/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        productService.softDelete(id);
        return ResponseEntity.ok("Đã xóa sản phẩm id = " + id);   // HTTP 200 + thông báo
    }

    // Danh sách cho KHÁCH: chỉ sản phẩm còn bán (deletedAt = null).
    // GET http://localhost:8111/api/products
    @GetMapping
    public ResponseEntity<List<Product>> getAllActive() {
        return ResponseEntity.ok(productService.findAllActive());
    }

    // Danh sách cho ADMIN: xem tất cả, kể cả hàng đã xóa mềm.
    // GET http://localhost:8111/api/products/all
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    // Mở bán sản phẩm: đổi status -> ACTIVE. Chỉ cần id trên URL, không cần body.
    // PATCH http://localhost:8111/api/products/1/activate
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Product> activate(@PathVariable Long id) {
        return ResponseEntity.ok(productService.activate(id));
    }

    // Ngừng bán sản phẩm: đổi status -> INACTIVE (sản phẩm vẫn hiện, chỉ không cho mua).
    // PATCH http://localhost:8111/api/products/1/deactivate
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Product> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deactivate(id));
    }
}
