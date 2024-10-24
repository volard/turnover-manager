package com.liberty.turnovermanagement.customers.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.liberty.turnovermanagement.DateTimeStringConverter;
import com.liberty.turnovermanagement.products.data.Product;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "customers")
public class Customer implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String surname;
    private String name;
    private String middleName;
    private String phone;
    private String email;
    private boolean isDeleted = false;
    private long version = 1; // Add this line
    @TypeConverters(DateTimeStringConverter.class)
    private LocalDateTime lastUpdated; // Add this line

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


    public boolean isDeleted() {
        return isDeleted;
    }

    public Customer(String surname, String name, String middleName, String phone, String email, boolean isDeleted) {
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
        this.phone = phone;
        this.email = email;
        this.isDeleted = isDeleted;
    }

    @Ignore
    public Customer(String surname, String name, String middleName, String phone, String email) {
        this(surname, name, middleName, phone, email, false);
    }

    @Ignore
    public Customer(){}


    // Getters
    public long getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setDeleted(boolean state){
        this.isDeleted = state;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return  "[ "+ id + " ]\n" +
                "Fullname: " + surname + ' ' + name + " " + middleName +
                "\nPhone: "  + phone +
                "\nEmail: " + email + (isDeleted ? "\nDELETED" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }
}

