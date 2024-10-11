package com.liberty.turnovermanagement.orders;

import androidx.annotation.NonNull;

import com.liberty.turnovermanagement.customers.Customer;
import com.liberty.turnovermanagement.products.Product;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order  implements Serializable {
    private long id;
    private Product product;
    private int amount;
    private Customer customer;
    private LocalDateTime datetime;
    private String city;
    private String street;
    private String home;

    // Default constructor
    public Order() {
    }

    // Constructor with all fields
    public Order(long id, Product product, int amount, Customer customer, LocalDateTime datetime, String city, String street, String home) {
        this.id = id;
        this.product = product;
        this.amount = amount;
        this.customer = customer;
        this.datetime = datetime;
        this.city = city;
        this.street = street;
        this.home = home;
    }

    // Getters
    public long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHome() {
        return home;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHome(String home) {
        this.home = home;
    }

    @NonNull
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", product=" + (product != null ? product.getName() : "null") +
                ", amount=" + amount +
                ", customer=" + (customer != null ? customer.getSurname() + ", " + customer.getName() : "null") +
                ", datetime=" + (datetime != null ? datetime.toString() : "null") +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", home='" + home + '\'' +
                '}';
    }

}

