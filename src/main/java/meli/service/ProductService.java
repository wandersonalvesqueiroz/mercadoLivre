package meli.service;

import meli.dto.ProductDetail;
import meli.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Optional<ProductDetail> findById(String id);
}
