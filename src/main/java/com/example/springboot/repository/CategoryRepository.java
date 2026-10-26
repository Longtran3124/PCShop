package com.example.springboot.repository;

import com.example.springboot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Lấy tất cả danh mục còn tồn tại (chưa xóa mềm)
    // Trả list dùng cho khách hàng
    List<Category> findByDeletedAtIsNull();

    // Lấy đúng 1 danh mục theo id, và phải chưa bị xóa mềm
    // Dùng cho: xem chi tiết 1 danh mục
    Optional<Category> findByIdAndDeletedAtIsNull(Long id);

    // Lấy ĐÚNG 1 danh mục theo slug. chưa bị xóa mềm
    // Trả Optional vì slug là UNIQUE -> nhiều nhất 1 cái.
    // Dùng cho: GET /categories/slug/laptop-gaming
    Optional<Category> findBySlugAndDeletedAtIsNull(String slug);

    // Tìm các danh mục có TÊN CHỨA keyword
    // Trả List vì có thể khớp nhiều cái (vd "laptop" khớp "Laptop", "Laptop Gaming"...).
    // Dùng cho: ô tìm kiếm GET /categories/search?keyword=laptop
    List<Category> findByNameContainingAndDeletedAtIsNull(String keyword);

    // Lấy các danh mục ĐÃ bị xóa mềm
    // Ngược với findByDeletedAtIsNull. Dùng cho: GET /categories/trash (admin).
    List<Category> findByDeletedAtIsNotNull();
}
