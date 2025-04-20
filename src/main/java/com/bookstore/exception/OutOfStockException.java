package com.bookstore.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(int bookId, int requested, int available) {
        super("Book with ID " + bookId + " has insufficient stock. Requested: " +
                requested + ", Available: " + available);
    }
}