package com.liberty.turnovermanagement.products.data;

import android.app.Application;
import com.liberty.turnovermanagement.AppDatabase;
import java.time.LocalDateTime;

public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        productDao = db.productDao();
    }

    public boolean updateProduct(Product newProduct) {
        // Get current product from database
        Product currentProduct = productDao.getProductById(newProduct.getId());


        // Check if there are actual changes
        if (!hasChanges(currentProduct, newProduct)) {
            return false;
        }

        // Create history record
        ProductHistory history = new ProductHistory();
        history.setProductId(currentProduct.getId());
        history.setName(currentProduct.getName());
        history.setAmount(currentProduct.getAmount());
        history.setPrice(currentProduct.getPrice());
        history.setVersion(currentProduct.getVersion());
        history.setCreatedAt(currentProduct.getLastUpdated());

        // Insert history record
        productDao.insertHistory(
                history.getProductId(),
                history.getName(),
                history.getAmount(),
                history.getPrice(),
                history.getVersion(),
                history.getCreatedAt()
        );

        // Update product with new values
        LocalDateTime now = LocalDateTime.now();
        long newVersion = currentProduct.getVersion() + 1;

        productDao.update(
                newProduct.getId(),
                newProduct.getName(),
                newProduct.getAmount(),
                newProduct.getPrice(),
                newVersion,
                now
        );

        return true;
    }

    private boolean hasChanges(Product currentProduct, Product newProduct) {
        return !currentProduct.getName().equals(newProduct.getName()) ||
                currentProduct.getAmount() != newProduct.getAmount() ||
                currentProduct.getPrice() != newProduct.getPrice();
    }

}
