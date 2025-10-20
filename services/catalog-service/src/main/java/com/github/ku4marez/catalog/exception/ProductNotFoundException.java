package com.github.ku4marez.catalog.exception;

import com.github.ku4marez.ecom.starters.web.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(String id) {
        super("Produce with id " + id + " not found");
    }
}
