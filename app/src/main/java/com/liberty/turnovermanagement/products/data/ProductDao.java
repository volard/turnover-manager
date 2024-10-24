package com.liberty.turnovermanagement.products.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products WHERE isDeleted = 0")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM products WHERE id = :productId")
    Product getProductById(long productId);

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAbsolutelyAll();

    @Insert
    Long insert(Product product);

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE isDeleted = 0 LIMIT 1)")
    LiveData<Boolean> hasAny();

    @Query("UPDATE products SET name = :name, amount = :amount, price = :price, version = :newVersion, lastUpdated = :lastUpdated WHERE id = :productId")
    void update(long productId, String name, int amount, double price, int newVersion, LocalDateTime lastUpdated);

    @Query("INSERT INTO product_history (productId, name, amount, price, version, updatedAt) VALUES (:productId, :name, :amount, :price, :version, :updatedAt)")
    void insertHistory(long productId, String name, int amount, double price, int version, LocalDateTime updatedAt);

    @Query("SELECT * FROM product_history WHERE productId = :productId ORDER BY version DESC")
    List<ProductHistory> getProductHistory(long productId);

    @Query("SELECT version FROM products WHERE id = :productId")
    long getProductVersion(long productId);

    @Query("UPDATE products SET isDeleted = 1 WHERE id = :productId")
    void softDelete(long productId);

    @Query("DELETE FROM products")
    void deleteAll();

    @Insert
    List<Long> insertAllAndGetIds(List<Product> products);
}
