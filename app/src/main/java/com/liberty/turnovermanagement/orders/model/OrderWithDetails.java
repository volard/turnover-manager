package com.liberty.turnovermanagement.orders.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.liberty.turnovermanagement.customers.Customer;
import com.liberty.turnovermanagement.products.Product;

/**
 * Associated Customer and Product data along with the Order, that represents the relationship
 */
public class OrderWithDetails {
    @Embedded
    public Order order;

    @Relation(
            parentColumn = "customerId",
            entityColumn = "id"
    )
    public Customer customer;

    @Relation(
            parentColumn = "productId",
            entityColumn = "id"
    )
    public Product product;
}