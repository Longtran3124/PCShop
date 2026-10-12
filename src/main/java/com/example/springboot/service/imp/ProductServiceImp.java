package com.example.springboot.service.imp;

import com.example.springboot.entity.Product;
import com.example.springboot.enums.ProductStatus;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product getById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("không tìm thấy sản phẩm với id: " + id));
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByNameContaining(String keyword) {
        return productRepository.findByNameContainingAndDeletedAtIsNull(keyword);
    }

    @Override
    public Product create(Product product) {
        product.setSoldCount(0); // Sản phẩm mới chưa bán cái nào
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.DRAFT);
        }
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product existing = getById(id); // sử dụng getById để kiểm tra xem sản phẩm này đã tồn tại chưa
        // Những field Client được phép chỉnh sửa
        if (product.getName() != null) {
            existing.setName(product.getName());
        }
        if (product.getSalePrice() != null) {
            existing.setSalePrice(product.getSalePrice());
        }
        if (product.getStockQuantity() != null) { // client không gửi thì bỏ qua và giữ tồn kho
            existing.setStockQuantity(product.getStockQuantity());
        }
        return productRepository.save(existing);
    }

    @Override
    public void softDelete(Long id) {
        Product existing = getById(id);
        existing.setDeletedAt(LocalDateTime.now()); // đánh dấu là đã xóa
        productRepository.save(existing);
    }

    @Override
    public List<Product> findAllActive() {
        return productRepository.findByDeletedAtIsNull(); // chỉ hàng tồn tại
    }

    @Override
    public Product activate(Long id) {
        Product existing = getById(id);
        existing.setStatus(ProductStatus.ACTIVE); // chuyển đổi sản phẩm từ mới nhập hàng về sang bán
        return productRepository.save(existing);
    }

    @Override
    public Product deactivate(Long id) {
        Product existing = getById(id);
        existing.setStatus(ProductStatus.INACTIVE); // chuyển từ bán sản phẩm sang ngừng bán
        return productRepository.save(existing);
    }
}
