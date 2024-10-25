package com.liberty.turnovermanagement.products.list;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;

import java.util.ArrayList;
import java.util.List;

public class ProductListViewModel extends AndroidViewModel {
    private final ProductDao productDao;
    private LiveData<List<Product>> products;
    private SharedPreferences sharedPreferences;


    public ProductListViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        productDao = db.productDao();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        updateProductList();

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }



    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            (sharedPreferences, key) -> {
                if ("isArchivedVisible".equals(key)) {
                    updateProductList();
                }
            };

    private void updateProductList() {
        boolean isArchivedVisible = sharedPreferences.getBoolean("isArchivedVisible", false);
        products = isArchivedVisible ? productDao.getAbsolutelyAll() : productDao.getAll();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
