package com.study.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.restapi.model.Product;
import com.study.restapi.services.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService mock_productService;

    @Test
    void getAllProducts_whenThereAreNoProducts_shouldReturnOkAndEmptyList() throws Exception {
        when(mock_productService.listProducts()).thenReturn(List.of());

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    void getAllProducts_whenThereAreProducts_shouldReturnOkListOfProducts() throws Exception {
        Product product = new Product("test", "test", "test", "test");

        when(mock_productService.listProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", equalTo(product.getName())))
                .andExpect(jsonPath("$.[0].description", equalTo(product.getDescription())))
                .andExpect(jsonPath("$.[0].brand", equalTo(product.getBrand())))
                .andExpect(jsonPath("$.[0].department", equalTo(product.getDepartment())));
    }

    @Test
    void getProductById_whenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        when(mock_productService.getProduct(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getProductById_whenProductExists_shouldReturnOkAndProduct() throws Exception {
        Product product = new Product("test", "test", "test", "test");

        when(mock_productService.getProduct(1)).thenReturn(product);

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.description", equalTo(product.getDescription())))
                .andExpect(jsonPath("$.brand", equalTo(product.getBrand())))
                .andExpect(jsonPath("$.department", equalTo(product.getDepartment())));
    }

    @Test
    void createProduct_whenProductIsSent_shouldReturnOkAndCreatedProduct() throws Exception {
        Product product = new Product("test", "test", "test", "test");

        when(mock_productService.createProduct(any())).thenReturn(product);

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.description", equalTo(product.getDescription())))
                .andExpect(jsonPath("$.brand", equalTo(product.getBrand())))
                .andExpect(jsonPath("$.department", equalTo(product.getDepartment())));
    }

    @Test
    void updateProduct_whenProductExists_shouldReturnOk() throws Exception {
        Product product = new Product("test", "test", "test", "test");

        when(mock_productService.updateProduct(eq(1L), any()))
                .thenReturn(product);

        mockMvc.perform(put("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.description", equalTo(product.getDescription())))
                .andExpect(jsonPath("$.brand", equalTo(product.getBrand())))
                .andExpect(jsonPath("$.department", equalTo(product.getDepartment())));

    }

    @Test
    void updateProduct_whenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        Product product = new Product("test", "test", "test", "test");

        when(mock_productService.updateProduct(eq(1L), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void deleteProduct_whenProductExists_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_whenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(mock_productService).removeProduct(1L);

        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

}