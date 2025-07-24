package meli.controller;

import lombok.extern.slf4j.Slf4j;
import meli.dto.ProductDetail;
import meli.model.Product;
import meli.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> listAll() {
        log.info("Listar todos os produtos");
        List<Product> products = productService.findAll();
        log.debug("Total de produtos: {}", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getProductById(@PathVariable String id) {
        log.info("Buscar produto id: {}", id);

        return productService.findById(id)
                .map(detail -> {
                    log.debug("Produto encontrado: {}", detail.getTitle());
                    return ResponseEntity.ok(detail);
                })
                .orElseGet(() -> {
                    log.warn("Produto id '{}' não encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
