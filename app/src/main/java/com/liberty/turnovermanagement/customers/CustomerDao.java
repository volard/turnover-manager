package com.liberty.turnovermanagement.customers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CustomerDao {
    @Query("SELECT * FROM customers WHERE isDeleted = 0")
    LiveData<List<Customer>> getAll();

    @Query("SELECT * FROM customers")
    LiveData<List<Customer>> getAbsolutelyAll();

    @Query("DELETE FROM customers")
    void deleteAll();

    @Insert
    List<Long> insertAllAndGetIds(List<Customer> customers);

    @Insert
    void insert(Customer customer);

    @Query("SELECT EXISTS(SELECT 1 FROM customers WHERE isDeleted = 0 LIMIT 1)")
    LiveData<Boolean> hasAny();

    @Update
    void update(Customer customer);

    @Query("UPDATE customers SET isDeleted = 1 WHERE id = :customerId")
    void softDelete(long customerId);
}
