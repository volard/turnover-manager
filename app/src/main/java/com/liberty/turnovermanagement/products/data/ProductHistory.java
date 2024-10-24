package com.liberty.turnovermanagement.products.data;

import androidx.room.Entity;
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
    private int version;
    @TypeConverters(DateTimeStringConverter.class)
    private LocalDateTime updatedAt;

    public void setProductId(long id) {
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

    public void setVersion(int version) {
        this.version = version;
    }

    public void setUpdatedAt(LocalDateTime lastUpdated) {
        this.updatedAt = lastUpdated;
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

    public int getVersion() {
        return version;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Constructors, getters, and setters
}
