package com.example.springboot.repository;

import com.example.springboot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingAndDeletedAtIsNull(String keyword);

    Optional<Product> findByIdAndDeletedAtIsNull(Long id);

    List<Product> findByDeletedAtIsNull();
}
