package com.liberty.turnovermanagement.products.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.liberty.turnovermanagement.DateTimeStringConverter;
import com.liberty.turnovermanagement.base.Identifiable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = "products")
public class Product implements Serializable, Identifiable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int amount;
    private double price;
    private boolean isDeleted;

    // Хранит номер последней версии. Всегда на один больше, чем имеющяяся крайнаяя запись в
    // product_history
    private long version = 1;

    // Дата первого изменения = дата создания
    @TypeConverters(DateTimeStringConverter.class)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    // Constructor with all fields
    public Product(String name, int amount, double price, boolean isDeleted) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.isDeleted = isDeleted;
    }

    // Add getters and setters for version and lastUpdated
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
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

    public void setDeleted(boolean state) {
        this.isDeleted = state;
    }

    @Ignore
    public Product() {
    }

    public ProductHistory toHistory() {
        ProductHistory ph = new ProductHistory();
        ph.setId(this.getId());
        ph.setName(this.getName());
        ph.setAmount(this.getAmount());
        ph.setPrice(this.getPrice());
        ph.setVersion(this.getVersion());
        ph.setCreatedAt(this.getLastUpdated());
        ph.setProductId(this.getId());
        return ph;
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


    @NonNull
    @Override
    public String toString() {
        return "[ " + id + " ]\n" + "Name: " + name + "\nAmount: " + amount + "\nPrice: " + price + " ₽" + (isDeleted ? "\nDELETED" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                isDeleted == product.isDeleted &&
                Objects.equals(name, product.name) &&
                Objects.equals(amount, product.amount) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, price, isDeleted);
    }

}
