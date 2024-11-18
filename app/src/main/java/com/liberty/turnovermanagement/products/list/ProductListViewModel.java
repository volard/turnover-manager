package com.liberty.turnovermanagement.products.list;

import android.app.Application;

import com.liberty.turnovermanagement.base.list.BaseListViewModel;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;

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
