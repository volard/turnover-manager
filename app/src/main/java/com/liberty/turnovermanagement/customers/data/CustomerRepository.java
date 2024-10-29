package com.liberty.turnovermanagement.customers.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.liberty.turnovermanagement.AppDatabase;
import java.time.LocalDateTime;

public class CustomerRepository {
    private final CustomerDao customerDao;

    public CustomerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        customerDao = db.customerDao();
    }

    public boolean update(Customer newCustomer) {
        Customer currentCustomer = customerDao.getCustomerById(newCustomer.getId());


        // Check if there are actual changes
        if (!hasChanges(currentCustomer, newCustomer)) {
            return false;
        }

        if (currentCustomer != null) {
            // Create history record
            CustomerHistory history = new CustomerHistory();
            history.setCustomerId(currentCustomer.getId());
            history.setSurname(currentCustomer.getSurname());
            history.setName(currentCustomer.getName());
            history.setMiddleName(currentCustomer.getMiddleName());
            history.setPhone(currentCustomer.getPhone());
            history.setEmail(currentCustomer.getEmail());
            history.setCreatedAt(currentCustomer.getLastUpdated());
            history.setVersion(currentCustomer.getVersion());

            // Insert history record
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

            // Update newCustomer with new version
            long newVersion = currentCustomer.getVersion() + 1;
            LocalDateTime now = LocalDateTime.now();
            customerDao.update(
                    newCustomer.getId(),
                    newCustomer.getSurname(),
                    newCustomer.getName(),
                    newCustomer.getMiddleName(),
                    newCustomer.getPhone(),
                    newCustomer.getEmail(),
                    newVersion,
                    now
            );
        }

        return true;
    }


    public boolean hasChanges(Customer currentCustomer, Customer newCustomer) {
        if (currentCustomer == null || newCustomer == null) {
            return false;
        }

        return !currentCustomer.getName().equals(newCustomer.getName()) ||
                !currentCustomer.getSurname().equals(newCustomer.getSurname()) ||
                !currentCustomer.getMiddleName().equals(newCustomer.getMiddleName()) ||
                !currentCustomer.getPhone().equals(newCustomer.getPhone()) ||
                !currentCustomer.getEmail().equals(newCustomer.getEmail());
    }


    public LiveData<Customer> getCustomerByIdAndVersion(long customerId, long version) {
        MutableLiveData<Customer> customer = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Customer currentCustomer = customerDao.getCustomerById(customerId);
            if (currentCustomer != null && currentCustomer.getVersion() == version) {
                customer.postValue(currentCustomer);
            } else {
                CustomerHistory historicalCustomer = customerDao.getCustomerHistoryByIdAndVersion(customerId, version);
                if (historicalCustomer != null) {
                    customer.postValue(historicalCustomer.getCustomer());
                }
            }
        });
        return customer;
    }

}
