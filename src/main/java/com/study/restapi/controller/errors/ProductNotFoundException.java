package com.study.restapi.controller.errors;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Product " + id + " could not be found");
    }

}
