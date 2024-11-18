package com.liberty.turnovermanagement.customers.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.liberty.turnovermanagement.DateTimeStringConverter;
import com.liberty.turnovermanagement.base.Constants;

import java.time.LocalDateTime;

@Entity(tableName = "customer_history")
public class CustomerHistory {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long customerId;

    private String surname;
    private String name;
    private String middleName;
    private String phone;
    private String email;

    @TypeConverters(DateTimeStringConverter.class)
    private LocalDateTime createdAt;

    private long version;

    @Ignore
    public Customer getCustomer() {

        Customer customer = new Customer();
        customer.setId(this.getCustomerId());
        customer.setSurname(this.getSurname());
        customer.setName(this.getName());
        customer.setMiddleName(this.getMiddleName());
        customer.setPhone(this.getPhone());
        customer.setEmail(this.getEmail());
        customer.setVersion(this.getVersion());
        return customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @NonNull
    @Override
    public String toString() {
        return "[ v" + version + " ] " + surname + " " + name + " " + middleName;
    }
}
