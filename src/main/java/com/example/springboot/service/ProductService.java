package com.example.springboot.service;

import com.example.springboot.dto.request.product.create.ProductCreateRequest;
import com.example.springboot.dto.request.product.filter.ProductFilter;
import com.example.springboot.dto.request.product.update.ProductUpdateRequest;
import com.example.springboot.dto.response.product.ProductResponse;
import com.example.springboot.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse getById(Long id);

    List<ProductResponse> findAll();

    List<ProductResponse> findByNameContaining(String keyword);

    ProductResponse create(ProductCreateRequest request);

    ProductResponse update(Long id, ProductUpdateRequest request);

    void softDelete(Long id);

    List<ProductResponse> findAllActive();

    ProductResponse activate(Long id); // mở bán -> status = ACTIVE

    ProductResponse deactivate(Long id); // Ngừng bán -> status = INACTIVE

    Page<ProductResponse> search(ProductFilter filter, Pageable pageable);

    ProductResponse getBySlug(String slug);

    // Khôi phục 1 sản phẩm từ thùng rác: đặt deletedAt = null -> hàng sống lại.
    // Nhận id, trả về ProductResponse của sản phẩm vừa khôi phục.
    ProductResponse restore(Long id);

    // Lấy danh sách hàng đã xóa mềm cho admin xem/khôi phục.
    List<ProductResponse> findTrash();
}
