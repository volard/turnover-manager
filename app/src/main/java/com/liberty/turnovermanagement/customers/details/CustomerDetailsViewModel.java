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

    public void loadCustomer(long customerId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Customer customer= customerDao.getCustomerById(customerId);
            selectedCustomer.postValue(customer);
        });
    }


    public LiveData<Customer> getSelectedCustomer() {
        return selectedCustomer;
    }


    public void updateCustomer(Customer updatedCustomer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Customer currentCustomer = customerDao.getCustomerById(updatedCustomer.getId());

            if (currentCustomer != null) {
                // Create a new history entry with the current customer's data
                CustomerHistory history = new CustomerHistory();
                history.setCustomerId(currentCustomer.getId());
                history.setSurname(currentCustomer.getSurname());
                history.setName(currentCustomer.getName());
                history.setMiddleName(currentCustomer.getMiddleName());
                history.setPhone(currentCustomer.getPhone());
                history.setEmail(currentCustomer.getEmail());
                history.setCreatedAt(currentCustomer.getLastUpdated());
                history.setVersion(currentCustomer.getVersion());

                // Insert the history entry
                customerDao.insertHistory(
                        history.getCustomerId(),
                        history.getSurname(),
                        history.getName(),
                        history.getMiddleName(),
                        history.getPhone(),
                        history.getEmail(),
                        history.getVersion(),
                        history.getCreatedAt()
                );

                // Update the current customer
                long newVersion = currentCustomer.getVersion() + 1;
                LocalDateTime now = LocalDateTime.now();
                customerDao.update(
                        updatedCustomer.getId(),
                        updatedCustomer.getSurname(),
                        updatedCustomer.getName(),
                        updatedCustomer.getMiddleName(),
                        updatedCustomer.getPhone(),
                        updatedCustomer.getEmail(),
                        newVersion,
                        now
                );

                // Fetch the updated customer and post it
                Customer updated = customerDao.getCustomerById(updatedCustomer.getId());
                updated.setVersion(newVersion);
                updated.setLastUpdated(now);
                selectedCustomer.postValue(updated);
            }
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
