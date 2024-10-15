package com.liberty.turnovermanagement.customers;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;

import java.util.List;

public class CustomersViewModel extends AndroidViewModel {
    private final CustomerDao customerDao;
    private final MutableLiveData<List<Customer>> customers = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;

    public CustomersViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        customerDao = db.customerDao();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        boolean isArchivedVisible = sharedPreferences.getBoolean("isArchivedVisible", false);
        if (isArchivedVisible){
            customers.setValue(customerDao.getAbsolutelyAll().getValue());
        }
        else {
            customers.setValue(customerDao.getAll().getValue());
        }
        // Set up a listener for preference changes
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if ("isArchivedVisible".equals(key)) {
                        boolean isArchivedVisible = sharedPreferences.getBoolean(key, false);
                        if (isArchivedVisible){
                            customers.setValue(customerDao.getAbsolutelyAll().getValue());
                        }
                        else {
                            customers.setValue(customerDao.getAll().getValue());
                        }
                    }
                }
            };

    public LiveData<List<Customer>> getCustomers() {
        return customers;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
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