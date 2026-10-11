package com.example.springboot.service;

import com.example.springboot.entity.Product;

import java.util.List;

public interface ProductService {

    Product getById(Long id);

    List<Product> findAll();

    List<Product> findByNameContaining(String keyword);
}
