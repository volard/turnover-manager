package com.liberty.turnovermanagement.customers.details;

import android.app.Application;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.base.details.BaseDetailsViewModel;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.customers.data.CustomerRepository;

import java.time.LocalDateTime;

public class CustomerDetailsViewModel extends BaseDetailsViewModel<Customer, CustomerHistory> {
    private final CustomerDao customerDao;
    private final CustomerRepository customerRepository;

    public CustomerDetailsViewModel(Application application) {
        super(application);
        customerDao = db.customerDao();
        customerRepository = new CustomerRepository(application);
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
            boolean result = customerRepository.update(customer);
            if (result){
                customer.setLastUpdated(LocalDateTime.now());
                selectedItem.postValue(customer);
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
