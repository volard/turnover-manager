package com.liberty.turnovermanagement.products.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.liberty.turnovermanagement.DateTimeStringConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "products")
public class Product implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int amount;
    private double price;
    private boolean isDeleted;
    private int version;
    @TypeConverters(DateTimeStringConverter.class)
    private LocalDateTime lastUpdated;

    // Constructor with all fields
    public Product(String name, int amount, double price, boolean isDeleted) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.isDeleted = isDeleted;
    }
    // Add getters and setters for version and lastUpdated
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    // Constructor without isDeleted (assuming false by default)
    @Ignore
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

    public void setDeleted(boolean state){
        this.isDeleted = state;
    }

    @Ignore
    public Product(){}

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


    @NonNull
    @Override
    public String toString() {
        return  "[ " + id + " ]\n" + "Name: " + name + "\nAmount: " + amount + "\nPrice: " + price + " ₽" + (isDeleted ? "\nDELETED" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }
}
