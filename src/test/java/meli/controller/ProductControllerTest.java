package meli.controller;

import meli.dto.ProductDetail;
import meli.model.Product;
import meli.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private static final String ID = "1";

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void shouldReturnProductListWhenCallingListAll() {
        Product product = new Product();
        when(productService.findAll())
                .thenReturn(List.of(product));

        ResponseEntity<List<Product>> response = productController.listAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(productService).findAll();
    }

    @Test
    void shouldReturnProductDetailWhenIdExists() {
        ProductDetail productDetail = ProductDetail.builder()
                .id(ID)
                .build();

        when(productService.findById(ID))
                .thenReturn(Optional.of(productDetail));

        ResponseEntity<ProductDetail> response = productController.getProductById(ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ID, response.getBody().getId());
        verify(productService).findById(ID);
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() {
        when(productService.findById(ID))
                .thenReturn(Optional.empty());

        ResponseEntity<ProductDetail> response = productController.getProductById(ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).findById(ID);
    }
}
