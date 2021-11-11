package com.study.restapi.controller;

import com.study.restapi.controller.dto.ProductDto;
import com.study.restapi.controller.errors.ProductNotFoundException;
import com.study.restapi.model.Product;
import com.study.restapi.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    Product getProductById(@PathVariable long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PostMapping
    ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto) {
        final var productSaved = productRepository.save(productDto.toEntity());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productSaved);
    }

    @PutMapping
    Product updateProduct(@RequestBody Product product) {
        long productId = product.getId();
        productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable long id) {
        productRepository.deleteById(id);
    }

}
