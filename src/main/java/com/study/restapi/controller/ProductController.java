package com.study.restapi.controller;

import com.study.restapi.controller.dto.ProductDto;
import com.study.restapi.model.Product;
import com.study.restapi.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    List<Product> getAllProducts() {
        return productService.listProducts();
    }

    @GetMapping("/{id}")
    Product getProductById(@PathVariable long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto) {
        final var productSaved = productService.createProduct(productDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productSaved);
    }

    @PutMapping("/{id}")
    Product updateProduct(@PathVariable long id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable long id) {
        productService.removeProduct(id);
    }
}
