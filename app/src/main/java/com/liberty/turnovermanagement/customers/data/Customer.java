package com.liberty.turnovermanagement.customers.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "customers")
public class Customer implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String surname;
    private String name;
    private String middleName;
    private String phone;
    private String email;
    private boolean isDeleted = false;
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
    public int getId() {
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
    public void setId(int id) {
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
}

