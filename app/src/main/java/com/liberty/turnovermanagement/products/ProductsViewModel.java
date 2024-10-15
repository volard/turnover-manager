package com.liberty.turnovermanagement.products;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductsViewModel extends AndroidViewModel {
    private final ProductDao productDao;

    private final MutableLiveData<List<Product>> products = new MutableLiveData<>();;

    private SharedPreferences sharedPreferences;

    public ProductsViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        productDao = db.productDao();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        boolean isArchivedVisible = sharedPreferences.getBoolean("isArchivedVisible", false);
        if (isArchivedVisible){
            products.setValue(productDao.getAbsolutelyAll().getValue());
        }
        else {
            products.setValue(productDao.getAll().getValue());
        }
        // Set up a listener for preference changes
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if ("isArchivedVisible".equals(key)) {
                        boolean isArchivedVisible = sharedPreferences.getBoolean(key, false);
                        if (isArchivedVisible){
                            products.setValue(productDao.getAbsolutelyAll().getValue());
                        }
                        else {
                            products.setValue(productDao.getAll().getValue());
                        }
                    }
                }
            };

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public void addNewProduct(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }

    private void populateWithFakeData() {
        ArrayList<Product> fakeProducts = new ArrayList<>();
        String[] productNames = {
                "Laptop", "Smartphone", "Tablet", "Desktop PC", "Wireless Earbuds",
                "Smart Watch", "Gaming Console", "4K TV", "Digital Camera", "Bluetooth Speaker",
                "Wireless Router", "External Hard Drive", "Printer", "Graphic Card", "Keyboard",
                "Mouse", "Monitor", "Headphones", "Webcam", "Microphone",
                "Power Bank", "USB Flash Drive", "SSD", "RAM", "CPU Cooler",
                "Smartwatch", "Fitness Tracker", "VR Headset", "Drone", "E-reader"
        };
        Random random = new Random();

        for (String name : productNames) {
            int amount = random.nextInt(20) + 1; // Random amount between 1 and 20
            double price = 50 + (950 * random.nextDouble()); // Random price between 50 and 1000
            price = Math.round(price * 100.0) / 100.0; // Round to 2 decimal places
            fakeProducts.add(new Product(name, amount, price));
        }
    }

    public void updateProduct(Product updatedProduct) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.update(updatedProduct);
        });
    }

    public void softDelete(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.softDelete(product.getId());
        });
    }

}