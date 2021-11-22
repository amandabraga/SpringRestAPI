package com.study.restapi.services;

import com.study.restapi.controller.dto.ProductDto;
import com.study.restapi.model.Product;
import com.study.restapi.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(long id) {
        return findProductById(id);
    }

    public Product createProduct(ProductDto productDto) {
        return productRepository.save(productDto.toEntity());
    }

    public Product updateProduct(long id, ProductDto productDto) {
        findProductById(id);

        Product product = productDto.toEntity();
        product.setId(id);

        return productRepository.save(product);
    }

    public void removeProduct(long id) {
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
