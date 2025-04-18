package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private List<OrderItem> items = new ArrayList<>();
    private double total;

    public Order() {}

    public Order(int id, int customerId, List<OrderItem> items, double total) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.total = total;
    }

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
