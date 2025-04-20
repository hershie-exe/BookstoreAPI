package com.bookstore.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(int customerId) {
        super("Cart for customer ID " + customerId + " not found.");
    }
}