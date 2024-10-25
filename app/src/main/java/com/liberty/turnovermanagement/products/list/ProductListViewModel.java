package com.liberty.turnovermanagement.products.list;

import android.app.Application;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.ui.BaseListViewModel;

public class ProductListViewModel extends BaseListViewModel<Product> {
    private final ProductDao productDao;

    public ProductListViewModel(Application application) {
        super(application);
        productDao = db.productDao();
        updateItemList();
    }

    @Override
    protected void updateItemList() {
        boolean isArchivedVisible = sharedPreferences.getBoolean("isArchivedVisible", false);
        items = isArchivedVisible ? productDao.getAbsolutelyAll() : productDao.getAll();
    }
}
