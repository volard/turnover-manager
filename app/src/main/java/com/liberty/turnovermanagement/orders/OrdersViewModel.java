package com.liberty.turnovermanagement.orders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.orders.model.Order;

import java.util.List;

public class OrdersViewModel extends AndroidViewModel {
    private final OrderDao orderDao;

    private final LiveData<List<Order>> orders;

    public OrdersViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        orderDao = db.orderDao();
        orders = orderDao.getAllOrders();
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
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