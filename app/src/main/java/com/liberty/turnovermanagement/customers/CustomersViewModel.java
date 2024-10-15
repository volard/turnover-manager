package com.liberty.turnovermanagement.customers;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.liberty.turnovermanagement.AppDatabase;

import java.util.List;

public class CustomersViewModel extends AndroidViewModel {
    private final CustomerDao customerDao;

    private final LiveData<List<Customer>> customers;

    public CustomersViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        customerDao = db.customerDao();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        boolean isArchivedVisible = sharedPreferences.getBoolean("isArchivedVisible", false);
        if (isArchivedVisible){
            customers = customerDao.getAbsolutelyAll();
        }
        else {
            customers = customerDao.getAll();
        }

    }

    public LiveData<List<Customer>> getCustomers() {
        return customers;
    }

    public void addNew(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            customerDao.insert(customer);
        });
    }

    public void update(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            customerDao.update(customer);
        });
    }

    public void softDelete(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            customerDao.softDelete(customer.getId());
        });
    }

}