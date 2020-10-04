package com.example.liquorexpress.models;

import java.util.List;

public class Order {
    private String id;
    private String userId;
    private String name;
    private String email;
    private double total;
    private boolean isComplete;
    private List<Cart> carts;

    public Order() {
    }

    public Order(String userId, String name, String email, double total, boolean isComplete, List<Cart> carts) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.total = total;
        this.carts = carts;
        this.isComplete = isComplete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }
}
