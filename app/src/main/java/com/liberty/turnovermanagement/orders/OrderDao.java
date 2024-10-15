package com.liberty.turnovermanagement.orders;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.liberty.turnovermanagement.orders.model.Order;
import com.liberty.turnovermanagement.orders.model.OrderWithDetails;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAllOrders();

    @Insert
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT EXISTS(SELECT 1 FROM orders LIMIT 1)")
    boolean hasAny();

    @Transaction
    @Query("SELECT * FROM orders")
    List<OrderWithDetails> getOrdersWithDetails();

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    OrderWithDetails getOrderWithDetailsById(int orderId);
}
