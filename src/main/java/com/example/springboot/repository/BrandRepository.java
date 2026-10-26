package com.example.springboot.repository;

import com.example.springboot.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findByDeletedAtIsNull();

    Optional<Brand> findByIdAndDeletedAtIsNull(Long id);

    Optional<Brand> findBySlugAndDeletedAtIsNull(String slug);

    List<Brand> findByNameContainingAndDeletedAtIsNull(String keyword);

    List<Brand> findByDeletedAtIsNotNull();
}
