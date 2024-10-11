package com.liberty.turnovermanagement.customers;

import androidx.annotation.NonNull;

public class Customer {
    private int id;
    private String surname;
    private String name;
    private String middlename;
    private String phone;
    private String email;

    // Default constructor
    public Customer() {
    }

    // Constructor with all fields
    public Customer(int id, String surname, String name, String middlename, String phone, String email) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.middlename = middlename;
        this.phone = phone;
        this.email = email;
    }

    // Constructor without id (useful when id is auto-generated)
    public Customer(String surname, String name, String middlename, String phone, String email) {
        this.surname = surname;
        this.name = name;
        this.middlename = middlename;
        this.phone = phone;
        this.email = email;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
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
        return "Customer{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", middlename='" + middlename + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

