package com.study.restapi.controller;

import com.study.restapi.controller.dto.ProductDto;
import com.study.restapi.model.Product;
import com.study.restapi.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        return findProductById(id);
    }

    @PostMapping
    ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto) {
        final var productSaved = productRepository.save(productDto.toEntity());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productSaved);
    }

    @PutMapping("/{id}")
    Product updateProduct(@PathVariable long id, @RequestBody ProductDto productDto) {
        Product product = findProductById(id);

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setBrand(productDto.getBrand());
        product.setDepartment(productDto.getDepartment());

        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable long id) {
        findProductById(id);

        productRepository.deleteById(id);
    }

    private Product findProductById(long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));
    }

}
