package com.liberty.turnovermanagement.products;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products WHERE isDeleted = 0")
    LiveData<List<Product>> getAllProducts();

    @Insert
    void insert(Product product);

    @Query("SELECT EXISTS(SELECT 1 FROM products LIMIT 1)")
    LiveData<Boolean> hasAny();

    @Update
    void update(Product product);

    @Query("UPDATE products SET isDeleted = 1 WHERE id = :productId")
    void softDelete(long productId);
}
