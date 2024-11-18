package com.liberty.turnovermanagement.products.details;

import android.app.Application;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.base.details.BaseDetailsViewModel;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.products.data.ProductHistory;

import java.time.LocalDateTime;

public class ProductDetailViewModel extends BaseDetailsViewModel<Product, ProductHistory> {
    private final ProductDao productDao;

    public ProductDetailViewModel(Application application) {
        super(application);
        productDao = db.productDao();
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
            Product currentProduct = productDao.getProductById(product.getId());

            if (currentProduct != null) {
                ProductHistory history = new ProductHistory();
                history.setProductId(currentProduct.getId());
                history.setName(currentProduct.getName());
                history.setAmount(currentProduct.getAmount());
                history.setPrice(currentProduct.getPrice());
                history.setCreatedAt(currentProduct.getLastUpdated());
                history.setVersion(currentProduct.getVersion());

                productDao.insertHistory(
                        history.getProductId(),
                        history.getName(),
                        history.getAmount(),
                        history.getPrice(),
                        history.getVersion(),
                        history.getCreatedAt()
                );

                long newVersion = currentProduct.getVersion() + 1;
                LocalDateTime now = LocalDateTime.now();
                productDao.update(
                        product.getId(),
                        product.getName(),
                        product.getAmount(),
                        product.getPrice(),
                        newVersion,
                        now
                );

                Product updated = productDao.getProductById(product.getId());
                updated.setVersion(newVersion);
                updated.setLastUpdated(now);
                selectedItem.postValue(updated);
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
