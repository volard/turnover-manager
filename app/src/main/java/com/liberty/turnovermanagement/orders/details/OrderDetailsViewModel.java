package com.liberty.turnovermanagement.orders.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.data.OrderDao;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;

import java.util.List;

public class OrderDetailsViewModel extends AndroidViewModel {
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final MutableLiveData<Order> selectedOrder = new MutableLiveData<>();
    private final LiveData<List<Product>> products;
    private final LiveData<List<Customer>> customers;

    public OrderDetailsViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        orderDao       = db.orderDao();
        productDao     = db.productDao();
        customerDao    = db.customerDao();
        products       = productDao.getAll();
        customers      = customerDao.getAll();
    }

    public void setSelectedOrder(Order order) {
        selectedOrder.setValue(order);
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<List<Customer>> getCustomers() {
        return customers;
    }

    public LiveData<Order> getSelectedOrder() {
        return selectedOrder;
    }

    // In OrderDetailsViewModel.java

    public void updateSelectedOrderProduct(Product product) {
        Order currentOrder = selectedOrder.getValue();
        if (currentOrder != null) {
            currentOrder.setProductId(product.getId());
            selectedOrder.setValue(currentOrder);
        }
    }

    public void updateSelectedOrderCustomer(Customer customer) {
        Order currentOrder = selectedOrder.getValue();
        if (currentOrder != null) {
            currentOrder.setCustomerId(customer.getId());
            selectedOrder.setValue(currentOrder);
        }
    }


    public void updateOrder(Order updatedOrder) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDao.update(updatedOrder);
            selectedOrder.postValue(updatedOrder);
        });
    }

    public void addNewOrder(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDao.insert(order);
            selectedOrder.postValue(order);
        });
    }

    public void deleteOrder(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDao.delete(order);
        });
    }
}
