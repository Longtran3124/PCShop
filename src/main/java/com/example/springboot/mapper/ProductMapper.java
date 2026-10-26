package com.example.springboot.mapper;

import com.example.springboot.dto.request.product.create.ProductCreateRequest;
import com.example.springboot.dto.response.product.ProductResponse;
import com.example.springboot.entity.Product;

public class ProductMapper {
    // Request (Tạo mới) -> Entity
    // vào -> Nhận DTO khách gửi, dựng Entity để lưu DB
    public static Product toEntity(ProductCreateRequest req) {
        Product p = new Product();
        p.setSku(req.getSku());
        p.setName(req.getName());
        p.setCategoryId(req.getCategoryId());
        p.setBrandId(req.getBrandId());
        p.setSeriesId(req.getSeriesId());
        p.setOriginalPrice(req.getOriginalPrice());
        p.setSalePrice(req.getSalePrice());
        p.setStockQuantity(req.getStockQuantity());
        p.setWarrantyMonths(req.getWarrantyMonths());
        p.setThumbnailUrl(req.getThumbnailUrl());
        p.setDescription(req.getDescription());

        return p;
    }

    // Entity -> response (trả ra)
    // ra -> lấy Entity từ DB, dựng DTO trả khách
    public static ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .sku(p.getSku())
                .name(p.getName())
                .slug(p.getSlug())
                .salePrice(p.getSalePrice())
                .originalPrice(p.getOriginalPrice())
                .stockQuantity(p.getStockQuantity())
                .soldCount(p.getSoldCount())
                .status(p.getStatus())
                .thumbnailUrl(p.getThumbnailUrl())
                .build();
    }
}
