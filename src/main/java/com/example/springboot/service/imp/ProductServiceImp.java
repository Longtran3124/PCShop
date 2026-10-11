package com.example.springboot.service.imp;

import com.example.springboot.entity.Product;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImp implements ProductService {

    ProductRepository productRepository;

    @Override
    public Product getById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new RuntimeException("không tìm thấy sản phảm với id: " + id));
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByNameContaining(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }
}
