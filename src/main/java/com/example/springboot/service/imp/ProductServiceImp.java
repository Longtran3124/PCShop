package com.example.springboot.service.imp;

import com.example.springboot.dto.request.product.create.ProductCreateRequest;
import com.example.springboot.dto.request.product.filter.ProductFilter;
import com.example.springboot.dto.request.product.update.ProductUpdateRequest;
import com.example.springboot.dto.response.product.ProductResponse;
import com.example.springboot.entity.Product;
import com.example.springboot.enums.ProductStatus;
import com.example.springboot.mapper.ProductMapper;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.service.ProductService;
import com.example.springboot.specification.ProductSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductRepository productRepository;

    // hỗ trợ nội bộ: trả entity để các method khác sửa/save
    private Product getEntityById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("không tìm thấy sản phẩm với id: " + id));
    }

    @Override
    public ProductResponse getById(Long id) {
        return ProductMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponse> findByNameContaining(String keyword) {
        return productRepository.findByNameContainingAndDeletedAtIsNull(keyword)
                .stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    public ProductResponse create(ProductCreateRequest request) {
        Product product = ProductMapper.toEntity(request); // DTO -> entity
        // server tự quản các field hệ thống
        product.setSlug(generateSlug(request.getName()));
        product.setSoldCount(0);
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0);
        }
        product.setStatus(ProductStatus.DRAFT);              // luôn DRAFT, không nhận từ client
        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product existing = getEntityById(id);
        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getSalePrice() != null) {
            existing.setSalePrice(request.getSalePrice());
        }
        if (request.getStockQuantity() != null) {
            existing.setStockQuantity(request.getStockQuantity());
        }
        Product saved = productRepository.save(existing);
        return ProductMapper.toResponse(saved);
    }


    @Override
    public void softDelete(Long id) {
        Product existing = getEntityById(id);
        existing.setDeletedAt(LocalDateTime.now()); // đánh dấu là đã xóa
        productRepository.save(existing);
    }

    @Override
    public List<ProductResponse> findAllActive() {
        return productRepository.findByDeletedAtIsNull()
                .stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    public ProductResponse activate(Long id) {
        Product existing = getEntityById(id);
        existing.setStatus(ProductStatus.ACTIVE); // chuyển đổi sản phẩm từ mới nhập hàng về sang bán
        return ProductMapper.toResponse(productRepository.save(existing));
    }

    @Override
    public ProductResponse deactivate(Long id) {
        Product existing = getEntityById(id);
        existing.setStatus(ProductStatus.INACTIVE); // chuyển từ bán sản phẩm sang ngừng bán
        return ProductMapper.toResponse(productRepository.save(existing));
    }

    @Override
    public Page<ProductResponse> search(ProductFilter filter, Pageable pageable) {
        return productRepository
                .findAll(ProductSpecification.withFilter(filter), pageable)
                .map(ProductMapper::toResponse);
    }

    private String generateSlug(String name) {
        return name.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
