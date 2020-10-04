package com.example.liquorexpress.models;

public class Cart {
    private String id;
    private String userId;
    private String productId;
    private String name;
    private double itemPrice;
    private long quantity;

    public Cart() {
    }

    public Cart(String userId, String productId, String name, double itemPrice, long quantity) {
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
