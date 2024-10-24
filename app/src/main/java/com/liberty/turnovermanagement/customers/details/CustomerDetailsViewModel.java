package com.liberty.turnovermanagement.customers.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;

import java.time.LocalDateTime;
import java.util.List;

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
            Customer currentCustomer = customerDao.getCustomerById(updatedCustomer.getId());

            // Create a new history entry
            CustomerHistory history = new CustomerHistory();
            history.setCustomerId(updatedCustomer.getId());
            history.setSurname(updatedCustomer.getSurname());
            history.setName(updatedCustomer.getName());
            history.setMiddleName(updatedCustomer.getMiddleName());
            history.setPhone(updatedCustomer.getPhone());
            history.setEmail(updatedCustomer.getEmail());
            history.setCreatedAt(LocalDateTime.now());
            history.setVersion(updatedCustomer.getVersion() + 1L);

             // Update the current product
            long newVersion = currentCustomer.getVersion() + 1;
            LocalDateTime now = LocalDateTime.now();
            customerDao.update(
                updatedCustomer.getId(),
                updatedCustomer.getSurname(),
                updatedCustomer.getName(),
                updatedCustomer.getMiddleName(),
                updatedCustomer.getPhone(),
                updatedCustomer.getEmail(),
                updatedCustomer.getVersion(),
                updatedCustomer.getLastUpdated()
            );

            // Fetch the updated product and post it
            Customer updated = customerDao.getCustomerById(updatedCustomer.getId());
            selectedCustomer.postValue(updated);
        });
    }

    public void softDelete(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            customerDao.softDelete(customer.getId());
            customer.setDeleted(true);
            selectedCustomer.postValue(customer);
        });
    }


    public LiveData<List<CustomerHistory>> getCustomerHistory(long customerId) {
        MutableLiveData<List<CustomerHistory>> history = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<CustomerHistory> customerHistory = customerDao.getCustomerHistory(customerId);
            history.postValue(customerHistory);
        });
        return history;
    }

    public void addNewCustomer(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long id = customerDao.insert(customer);
            customer.setId((int) id);
            selectedCustomer.postValue(customer);
        });
    }
}
