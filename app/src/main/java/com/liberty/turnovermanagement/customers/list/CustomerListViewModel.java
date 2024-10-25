package com.liberty.turnovermanagement.customers.list;

import android.app.Application;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.base.list.BaseListViewModel;

public class CustomerListViewModel extends BaseListViewModel<Customer> {
    private final CustomerDao customerDao;

    public CustomerListViewModel(Application application) {
        super(application);
        customerDao = db.customerDao();
        updateItemList();
    }

    @Override
    protected void updateItemList() {
        boolean isArchivedVisible = sharedPreferences.getBoolean("isArchivedVisible", false);
        items = isArchivedVisible ? customerDao.getAbsolutelyAll() : customerDao.getAll();
    }
}
