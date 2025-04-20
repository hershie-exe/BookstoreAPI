package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int customerId;
    private List<Item> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(int customerId, List<Item> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private int bookId;
        private int quantity;

        public Item() {}

        public Item(int bookId, int quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}