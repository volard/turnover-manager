package com.liberty.turnovermanagement.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ProductsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Product>> items;

    public ProductsViewModel() {
        items = new MutableLiveData<>();
        items.setValue(new ArrayList<>());
    }

    public LiveData<ArrayList<Product>> getItems() {
        return items;
    }

    public void addNewItem() {
        ArrayList<Product> currentList = items.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        Product newProduct = new Product("another one " + currentList.size());
        currentList.add(newProduct);
        items.setValue(currentList);
    }
}