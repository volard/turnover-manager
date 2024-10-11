package com.liberty.turnovermanagement.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Random;

public class ProductsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Product>> products;

    public ProductsViewModel() {
        products = new MutableLiveData<>();
        products.setValue(new ArrayList<>());
        populateWithFakeData();
    }

    public LiveData<ArrayList<Product>> getProducts() {
        return products;
    }

    public void addNewProduct(Product product) {
        ArrayList<Product> currentList = products.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(product);
        products.setValue(currentList);
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

        products.setValue(fakeProducts);
    }

    public void updateProduct(Product updatedProduct) {
        ArrayList<Product> currentList = products.getValue();
        if (currentList != null) {
            for (int i = 0; i < currentList.size(); i++) {
                if (currentList.get(i).getName().equals(updatedProduct.getName())) {
                    currentList.set(i, updatedProduct);
                    break;
                }
            }
            products.setValue(currentList);
        }
    }

}