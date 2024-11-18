package com.liberty.turnovermanagement.products.details;

import android.app.Application;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.base.details.BaseDetailsViewModel;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.products.data.ProductHistory;
import com.liberty.turnovermanagement.products.data.ProductRepository;

import java.time.LocalDateTime;

public class ProductDetailViewModel extends BaseDetailsViewModel<Product, ProductHistory> {
    private final ProductDao productDao;
    private final ProductRepository productRepository;

    public ProductDetailViewModel(Application application) {
        super(application);
        productDao = db.productDao();
        productRepository = new ProductRepository(application);
    }

    @Override
    public void loadItem(long itemId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Product product = productDao.getProductById(itemId);
            selectedItem.postValue(product);
        });
    }

    @Override
    public void updateItem(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            boolean result = productRepository.updateProduct(product);
            if (result){
                product.setLastUpdated(LocalDateTime.now());
                selectedItem.postValue(product);
            }
        });
    }

    @Override
    public void softDelete(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.softDelete(product.getId());
            product.setDeleted(true);
            selectedItem.postValue(product);
        });
    }

    @Override
    public void addNewItem(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long id = productDao.insert(product);
            product.setId(id);
            selectedItem.postValue(product);
        });
    }
}
