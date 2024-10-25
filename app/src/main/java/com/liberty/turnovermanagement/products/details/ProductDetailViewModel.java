package com.liberty.turnovermanagement.products.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.products.data.ProductHistory;

import java.time.LocalDateTime;
import java.util.List;

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
            Product currentProduct = productDao.getProductById(updatedProduct.getId());

            if (currentProduct != null) {
                // Create a new history entry with the current customer's data
                ProductHistory history = new ProductHistory();
                history.setProductId(currentProduct.getId());
                history.setName(currentProduct.getName());
                history.setAmount(currentProduct.getAmount());
                history.setPrice(currentProduct.getPrice());
                history.setCreatedAt(currentProduct.getLastUpdated());
                history.setVersion(currentProduct.getVersion());

                // Insert the history entry
                productDao.insertHistory(
                        history.getProductId(),
                        history.getName(),
                        history.getAmount(),
                        history.getPrice(),
                        history.getVersion(),
                        history.getCreatedAt()
                );

                // Update the current customer
                long newVersion = currentProduct.getVersion() + 1;
                LocalDateTime now = LocalDateTime.now();
                productDao.update(
                        updatedProduct.getId(),
                        updatedProduct.getName(),
                        updatedProduct.getAmount(),
                        updatedProduct.getPrice(),
                        newVersion,
                        now
                );

                // Fetch the updated customer and post it
                Product updated = productDao.getProductById(updatedProduct.getId());
                updated.setVersion(newVersion);
                updated.setLastUpdated(now);
                selectedProduct.postValue(updated);
            }
        });
    }

    public void loadProduct(long productId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Product product = productDao.getProductById(productId);
            selectedProduct.postValue(product);
        });
    }

    public LiveData<List<ProductHistory>> getProductHistory(long productId) {
        MutableLiveData<List<ProductHistory>> history = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ProductHistory> productHistory = productDao.getProductHistory(productId);
            history.postValue(productHistory);
        });
        return history;
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

