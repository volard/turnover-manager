package com.liberty.turnovermanagement.orders.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAllOrders();

    @Insert
    long insert(Order order);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    Order getOrderById(long orderId);

    @Update
    void update(Order order);

    @Query("DELETE FROM orders")
    void deleteAll();

    @Insert
    void insertAll(List<Order> orders);

    @Delete
    void delete(Order order);
}
