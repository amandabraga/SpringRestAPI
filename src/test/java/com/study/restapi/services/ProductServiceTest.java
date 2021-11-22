package com.study.restapi.services;

import com.study.restapi.controller.dto.ProductDto;
import com.study.restapi.model.Product;
import com.study.restapi.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository mock_productRepository;

    @Test
    void listProducts_whenDatabaseIsEmpty_shouldReturnEmptyList() {
        when(mock_productRepository.findAll()).thenReturn(List.of());

        ProductService productService = new ProductService(mock_productRepository);

        Assertions.assertThat(productService.listProducts()).isEmpty();
    }

    @Test
    void listProducts_whenDatabaseIsNotEmpty_shouldReturnListOfProducts() {
        Product product = new Product("test", "test", "test", "test");

        when(mock_productRepository.findAll()).thenReturn(List.of(product, product));

        ProductService productService = new ProductService(mock_productRepository);

        final var actual = productService.listProducts();

        Assertions.assertThat(actual).hasSize(2);
        Assertions.assertThat(actual.get(0)).isEqualTo(product);
    }

    @Test
    void getProduct_whenProductDoesNotExist_shouldReturnException() {
        when(mock_productRepository.findById(any())).thenReturn(Optional.empty());

        ProductService productService = new ProductService(mock_productRepository);

        var result = assertThrows(ResponseStatusException.class,
                () -> productService.getProduct(1));

        Assertions.assertThat(result.getReason()).isEqualToIgnoringCase("Product not found");
    }

    @Test
    void getProduct_whenProductExists_shouldReturnProduct() {
        Product product = new Product("test", "test", "test", "test");

        when(mock_productRepository.findById(any())).thenReturn(Optional.of(product));

        ProductService productService = new ProductService(mock_productRepository);

        Assertions.assertThat(productService.getProduct(1)).isEqualTo(product);
    }

    @Test
    void createProduct_whenProductIsSent_shouldCreateProduct() {
        ProductDto productDto = new ProductDto("test", "test", "test", "test");
        Product product = productDto.toEntity();

        when(mock_productRepository.save(any())).thenReturn(product);

        ProductService productService = new ProductService(mock_productRepository);

        Assertions.assertThat(productService.createProduct(productDto)).isEqualTo(product);
    }

    @Test
    void updateProduct_whenProductDoesNotExist_shouldThrowException() {
        when(mock_productRepository.findById(any())).thenReturn(Optional.empty());

        ProductService productService = new ProductService(mock_productRepository);

        var result = assertThrows(ResponseStatusException.class,
                () -> productService.updateProduct(1, Mockito.mock(ProductDto.class)));

        Assertions.assertThat(result.getReason()).isEqualToIgnoringCase("Product not found");
    }

    @Test
    void updateProduct_whenProductExists_shouldUpdateAndReturnProduct() {
        ProductDto productDto = new ProductDto("test", "test", "test", "test");
        Product product = productDto.toEntity();

        when(mock_productRepository.findById(any())).thenReturn(Optional.of(product));
        when(mock_productRepository.save(any())).thenReturn(product);

        ProductService productService = new ProductService(mock_productRepository);

        Assertions.assertThat(productService.updateProduct(1, productDto))
                .isEqualTo(product);
    }

    @Test
    void removeProduct_whenProductDoesNotExist_shouldThrowException() {
        when(mock_productRepository.findById(any())).thenReturn(Optional.empty());

        ProductService productService = new ProductService(mock_productRepository);

        var result = assertThrows(ResponseStatusException.class,
                () -> productService.removeProduct(1));

        Assertions.assertThat(result.getReason()).isEqualToIgnoringCase("Product not found");
    }

    @Test
    void removeProduct_whenProductExists_shouldUpdateAndReturnProduct() {
        ProductDto productDto = new ProductDto("test", "test", "test", "test");
        Product product = productDto.toEntity();

        when(mock_productRepository.findById(any())).thenReturn(Optional.of(product));

        ProductService productService = new ProductService(mock_productRepository);

        assertDoesNotThrow(() -> productService.removeProduct(1));
    }
}
