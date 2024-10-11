package com.liberty.turnovermanagement.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ProductsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Product>> products;

    public ProductsViewModel() {
        products = new MutableLiveData<>();
        products.setValue(new ArrayList<>());
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
}