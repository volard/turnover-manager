package com.liberty.turnovermanagement.orders.list;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.data.OrderDao;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.base.list.BaseListViewModel;


public class OrderListViewModel extends BaseListViewModel<Order> {
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final MediatorLiveData<Boolean> canCreateOrder;

    public OrderListViewModel(Application application) {
        super(application);
        orderDao = db.orderDao();
        productDao = db.productDao();
        customerDao = db.customerDao();

        canCreateOrder = new MediatorLiveData<>();
        LiveData<Boolean> hasCustomers = customerDao.hasAny();
        LiveData<Boolean> hasProducts = productDao.hasAny();

        canCreateOrder.addSource(hasCustomers, customers -> updateCanCreateOrder(customers, hasProducts.getValue()));
        canCreateOrder.addSource(hasProducts, products -> updateCanCreateOrder(hasCustomers.getValue(), products));

        updateItemList();
    }

    @Override
    protected void updateItemList() {
        items = orderDao.getAllOrders();
    }

    private void updateCanCreateOrder(Boolean customers, Boolean products) {
        canCreateOrder.setValue(customers != null && products != null && customers && products);
    }

    public LiveData<Boolean> canCreateOrder() {
        return canCreateOrder;
    }

    public void delete(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> orderDao.delete(order));
    }
}

