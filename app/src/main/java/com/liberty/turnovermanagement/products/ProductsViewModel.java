package com.liberty.turnovermanagement.products;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.liberty.turnovermanagement.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductsViewModel extends AndroidViewModel {
    private final ProductDao productDao;

    private final LiveData<List<Product>> products;

    public ProductsViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        productDao = db.productDao();
        products = productDao.getAllProducts();
    }

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

//        products.setValue(fakeProducts);
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