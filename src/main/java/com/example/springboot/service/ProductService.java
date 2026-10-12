package com.example.springboot.service;

import com.example.springboot.entity.Product;

import java.util.List;

public interface ProductService {

    Product getById(Long id);

    List<Product> findAll();

    List<Product> findByNameContaining(String keyword);

    Product create(Product product);

    Product update(Long id, Product product);

    void softDelete(Long id);

    List<Product> findAllActive();

    Product activate(Long id); // mở bán -> status = ACTIVE

    Product deactivate(Long id); // Ngừng bán -> status = INACTIVE
}
