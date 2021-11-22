package com.study.restapi.controller.dto;

import com.study.restapi.model.Product;

public class ProductDto {

    private String name;
    private String description;
    private String brand;
    private String department;

    public ProductDto(String name, String description, String brand, String department) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getDepartment() {
        return department;
    }

    public Product toEntity() {
        return new Product(name, description, brand, department);
    }
}
