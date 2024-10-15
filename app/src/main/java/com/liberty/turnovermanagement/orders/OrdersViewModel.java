package com.liberty.turnovermanagement.orders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.customers.CustomerDao;
import com.liberty.turnovermanagement.orders.model.Order;
import com.liberty.turnovermanagement.products.ProductDao;

import java.util.List;

public class OrdersViewModel extends AndroidViewModel {
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;

    private final LiveData<List<Order>> orders;
    private final MediatorLiveData<Boolean> canCreateOrder;

    public OrdersViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        orderDao       = db.orderDao();
        productDao     = db.productDao();
        customerDao    = db.customerDao();
        orders         = orderDao.getAllOrders();
        canCreateOrder = new MediatorLiveData<>();
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
    }


    public LiveData<Boolean> canCreateOrder() {
        LiveData<Boolean> hasCustomers = customerDao.hasAny();
        LiveData<Boolean> hasProducts = productDao.hasAny();

        canCreateOrder.addSource(hasCustomers, customers -> {
            Boolean products = hasProducts.getValue();
            canCreateOrder.setValue(customers != null && products != null && customers && products);
        });

        canCreateOrder.addSource(hasProducts, products -> {
            Boolean customers = hasCustomers.getValue();
            canCreateOrder.setValue(customers != null && products != null && customers && products);
        });
        return canCreateOrder;
    }


    public void addNewProduct(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDao.insert(order);
        });
    }

    public void update(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDao.update(order);
        });
    }

    public void delete(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDao.delete(order);
        });
    }

}