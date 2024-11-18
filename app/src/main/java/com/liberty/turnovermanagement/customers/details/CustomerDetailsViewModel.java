package com.liberty.turnovermanagement.customers.details;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.base.details.BaseDetailsViewModel;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerDetailsViewModel extends BaseDetailsViewModel<Customer, CustomerHistory> {
    private final CustomerDao customerDao;

    public CustomerDetailsViewModel(Application application) {
        super(application);
        customerDao = db.customerDao();
    }

    @Override
    public void loadItem(long itemId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Customer customer = customerDao.getCustomerById(itemId);
            selectedItem.postValue(customer);
        });
    }

    @Override
    public void updateItem(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Customer currentCustomer = customerDao.getCustomerById(customer.getId());

            if (currentCustomer != null) {
                CustomerHistory history = new CustomerHistory();
                history.setCustomerId(currentCustomer.getId());
                history.setSurname(currentCustomer.getSurname());
                history.setName(currentCustomer.getName());
                history.setMiddleName(currentCustomer.getMiddleName());
                history.setPhone(currentCustomer.getPhone());
                history.setEmail(currentCustomer.getEmail());
                history.setCreatedAt(currentCustomer.getLastUpdated());
                history.setVersion(currentCustomer.getVersion());

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

                long newVersion = currentCustomer.getVersion() + 1;
                LocalDateTime now = LocalDateTime.now();
                customerDao.update(
                        customer.getId(),
                        customer.getSurname(),
                        customer.getName(),
                        customer.getMiddleName(),
                        customer.getPhone(),
                        customer.getEmail(),
                        newVersion,
                        now
                );

                Customer updated = customerDao.getCustomerById(customer.getId());
                updated.setVersion(newVersion);
                updated.setLastUpdated(now);
                selectedItem.postValue(updated);
            }
        });
    }

    @Override
    public void softDelete(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            customerDao.softDelete(customer.getId());
            customer.setDeleted(true);
            selectedItem.postValue(customer);
        });
    }


    @Override
    public void addNewItem(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long id = customerDao.insert(customer);
            customer.setId(id);
            selectedItem.postValue(customer);
        });
    }
}
