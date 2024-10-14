package com.liberty.turnovermanagement;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.liberty.turnovermanagement.customers.Customer;
import com.liberty.turnovermanagement.customers.CustomerDao;
import com.liberty.turnovermanagement.products.Product;
import com.liberty.turnovermanagement.products.ProductDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Product.class, Customer.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract CustomerDao customerDao();

    private static volatile AppDatabase INSTANCE;

    // Add this block
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "main_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

