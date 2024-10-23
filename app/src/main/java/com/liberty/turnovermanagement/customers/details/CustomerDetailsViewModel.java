package com.liberty.turnovermanagement.customers.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;

public class CustomerDetailsViewModel extends AndroidViewModel {
    private final CustomerDao customerDao;
    private final MutableLiveData<Customer> selectedCustomer = new MutableLiveData<>();

    public CustomerDetailsViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        customerDao = db.customerDao();
    }

    public void setSelectedCustomer(Customer customer) {
        selectedCustomer.setValue(customer);
    }

    public LiveData<Customer> getSelectedCustomer() {
        return selectedCustomer;
    }

    public void updateCustomer(Customer updatedCustomer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            customerDao.update(updatedCustomer);
            selectedCustomer.postValue(updatedCustomer);
        });
    }

    public void softDelete(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            customerDao.softDelete(customer.getId());
            customer.setDeleted(true);
            selectedCustomer.postValue(customer);
        });
    }

    public void addNewCustomer(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long id = customerDao.insert(customer);
            customer.setId((int) id);
            selectedCustomer.postValue(customer);
        });
    }
}
