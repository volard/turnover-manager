package com.liberty.turnovermanagement.products;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products WHERE isDeleted = 0")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAbsolutelyAll();

    @Insert
    void insert(Product product);

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE isDeleted = 0 LIMIT 1)")
    LiveData<Boolean> hasAny();

    @Update
    void update(Product product);

    @Query("UPDATE products SET isDeleted = 1 WHERE id = :productId")
    void softDelete(long productId);

    @Query("DELETE FROM products")
    void deleteAll();

    @Insert
    List<Long> insertAllAndGetIds(List<Product> products);
}
