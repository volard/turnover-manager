package com.liberty.turnovermanagement.customers.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface CustomerDao {
    @Query("SELECT * FROM customers WHERE isDeleted = 0")
    LiveData<List<Customer>> getAll();

    @Query("SELECT * FROM customers")
    LiveData<List<Customer>> getAbsolutelyAll();

    @Query("DELETE FROM customers")
    void deleteAll();

    @Query("SELECT * FROM customers WHERE id = :customerId")
    Customer getCustomerById(long customerId);

    @Query("UPDATE customers SET surname = :surname, name = :name, middleName = :middleName, phone = :phone, email = :email, version = :newVersion, lastUpdated = :lastUpdated WHERE id = :customerId")
    void update(long customerId, String surname, String name, String middleName, String phone, String email, long newVersion, LocalDateTime lastUpdated);

    @Query("INSERT INTO customer_history (customerId, surname, name, middleName, phone, email, version, createdAt) VALUES (:customerId, :surname, :name, :middleName, :phone, :email, :version, :updatedAt)")
    void insertHistory(long customerId, String surname, String name, String middleName, String phone, String email, int version, LocalDateTime updatedAt);

    @Query("SELECT * FROM customer_history WHERE customerId = :customerId AND version = :version")
    CustomerHistory getCustomerHistoryByIdAndVersion(long customerId, long version);

    @Query("SELECT * FROM customer_history WHERE customerId = :customerId ORDER BY version DESC")
    List<CustomerHistory> getCustomerHistory(long customerId);

    @Query("SELECT version FROM customers WHERE id = :customerId")
    long getCustomerVersion(long customerId);

    @Insert
    List<Long> insertAllAndGetIds(List<Customer> customers);

    @Insert
    long insert(Customer customer);

    @Query("SELECT EXISTS(SELECT 1 FROM customers WHERE isDeleted = 0 LIMIT 1)")
    LiveData<Boolean> hasAny();

    @Update
    void update(Customer customer);

    @Query("UPDATE customers SET isDeleted = 1 WHERE id = :customerId")
    void softDelete(long customerId);
}
