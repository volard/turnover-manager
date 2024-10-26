package com.liberty.turnovermanagement.products.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.liberty.turnovermanagement.DateTimeStringConverter;

import java.time.LocalDateTime;

@Entity(tableName = "product_history")
public class ProductHistory {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long productId;
    private String name;
    private int amount;
    private double price;
    private long version;
    @TypeConverters(DateTimeStringConverter.class)
    private LocalDateTime createdAt;

    public void setProductId(long id) {
        this.id = id;
    }

    @Ignore
    public Product getProduct(){
        Product product = new Product();
        product.setId(this.getId());
        product.setName(this.getName());
        product.setAmount(this.getAmount());
        product.setPrice(this.getPrice());
        product.setLastUpdated(this.getCreatedAt());
        product.setVersion(this.getVersion());
        return product;
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

    public void setVersion(long version) {
        this.version = version;
    }

    public void setCreatedAt(LocalDateTime lastUpdated) {
        this.createdAt = lastUpdated;
    }

    public long getProductId() {
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

    public long getVersion() {
        return version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Constructors, getters, and setters
}
