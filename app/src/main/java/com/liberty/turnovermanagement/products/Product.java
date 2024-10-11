package com.liberty.turnovermanagement.products;

import androidx.annotation.NonNull;

import com.liberty.turnovermanagement.Utils;

import java.util.concurrent.ThreadLocalRandom;

public class Product {
    private long id;
    private String name;
    private int amount;
    private double price;
    private boolean isDeleted;

    public Product() {
        id = Utils.generateUID();
    }

    // Default constructor
    public Product(String name) {
        this();
        this.name = name;
    }

    // Constructor with all fields
    public Product(String name, int amount, double price, boolean isDeleted) {
        this();
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.isDeleted = isDeleted;
    }

    // Constructor without isDeleted (assuming false by default)
    public Product(String name, int amount, double price) {
        this(name, amount, price, false);
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
