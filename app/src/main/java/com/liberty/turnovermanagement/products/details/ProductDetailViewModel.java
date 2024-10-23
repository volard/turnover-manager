package com.liberty.turnovermanagement.products.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;

public class ProductDetailViewModel extends AndroidViewModel {
    private final ProductDao productDao;
    private final MutableLiveData<Product> selectedProduct = new MutableLiveData<>();

    public ProductDetailViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        productDao = db.productDao();
    }

    public void setSelectedProduct(Product product) {
        selectedProduct.postValue(product);
    }

    public LiveData<Product> getSelectedProduct() {
        return selectedProduct;
    }

    public void updateProduct(Product updatedProduct) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.update(updatedProduct);
            selectedProduct.postValue(updatedProduct);
        });
    }

    public void softDelete(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.softDelete(product.getId());
            product.setDeleted(true);
            selectedProduct.postValue(product);
        });
    }

    public void addNewProduct(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long id = productDao.insert(product);
            product.setId(id);
            selectedProduct.postValue(product);
        });
    }
}

