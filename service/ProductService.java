package com.exemplo.api.service;

import com.exemplo.api.dto.ProductDetail;
import com.exemplo.api.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Optional<ProductDetail> findById(String id);
}
