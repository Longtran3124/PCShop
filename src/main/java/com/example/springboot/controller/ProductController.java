package com.example.springboot.controller;

import com.example.springboot.entity.Product;
import com.example.springboot.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // được phép các api
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    // http://localhost:8111/api/products/1
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    // http://localhost:8111/api/products
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    // http://localhost:8111/api/products/search?keyword=iphone
    @GetMapping("/search")
    public ResponseEntity<List<Product>> findByNameContaining(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.findByNameContaining(keyword));
    }
}
