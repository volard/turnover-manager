package com.liberty.turnovermanagement.customers.list;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;

import java.util.List;

public class CustomerListViewModel extends AndroidViewModel {
    private final CustomerDao customerDao;
    private LiveData<List<Customer>> customers;
    private SharedPreferences sharedPreferences;

    public CustomerListViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        customerDao = db.customerDao();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        updateCustomerList();

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            (sharedPreferences, key) -> {
                if ("isArchivedVisible".equals(key)) {
                    updateCustomerList();
                }
            };

    private void updateCustomerList() {
        boolean isArchivedVisible = sharedPreferences.getBoolean("isArchivedVisible", false);
        customers = isArchivedVisible ? customerDao.getAbsolutelyAll() : customerDao.getAll();
    }

    public LiveData<List<Customer>> getCustomers() {
        return customers;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
