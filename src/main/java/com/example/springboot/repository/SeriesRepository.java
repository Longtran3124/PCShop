package com.example.springboot.repository;

import com.example.springboot.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    // Lấy các series của 1 thương hiệu (vd tất cả dòng của ASUS)
    List<Series> findByBrandId(Long brandId);
    // Tìm theo tên
    List<Series> findByNameContaining(String keyword);
}
