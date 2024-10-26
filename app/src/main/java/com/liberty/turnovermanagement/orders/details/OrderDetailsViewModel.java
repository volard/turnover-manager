package com.liberty.turnovermanagement.orders.details;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.data.OrderDao;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.base.details.BaseDetailsViewModel;
import com.liberty.turnovermanagement.products.data.ProductHistory;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsViewModel extends BaseDetailsViewModel<Order, Void> {
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final LiveData<List<Product>> products;
    private final LiveData<List<Customer>> customers;
    private final MutableLiveData<Customer> customerForOrder= new MutableLiveData<>();
    private final MutableLiveData<Product> productForOrder = new MutableLiveData<>();

    public OrderDetailsViewModel(Application application) {
        super(application);
        orderDao = db.orderDao();
        productDao = db.productDao();
        customerDao = db.customerDao();
        products = productDao.getAll();
        customers = customerDao.getAll();
    }

    public LiveData<Product> getProductForOrder() {
        return productForOrder;
    }

    public void loadProductForOrder(long productId, long productVersion) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Product product = getProductForOrder(productId, productVersion);
            productForOrder.postValue(product);
        });
    }

    private Product getProductForOrder(long productId, long productVersion) {
        Product currentProduct = productDao.getProductById(productId);

        if (currentProduct != null && currentProduct.getVersion() == productVersion) {
            return currentProduct;
        } else {
            ProductHistory historicalProduct = productDao.getProductByIdAndVersion(productId, productVersion);
            if (historicalProduct != null) {
                return historicalProduct.getProduct();
            }
        }

        return null;
    }



    @Override
    public void loadItem(long itemId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Order order = orderDao.getOrderById(itemId);
            selectedItem.postValue(order);
        });
    }

    @Override
    public void updateItem(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDao.update(order);
            selectedItem.postValue(order);
        });
    }

    @Override
    public void softDelete(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDao.delete(order);
        });
    }

    @Override
    public LiveData<List<Void>> getItemHistory(long itemId) {
        // Orders don't have history in the current implementation
        return new MutableLiveData<>(new ArrayList<>());
    }

    @Override
    public void addNewItem(Order order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long productVersion = productDao.getProductVersion(order.getProductId());
            long customerVersion = customerDao.getCustomerVersion(order.getCustomerId());

            order.setProductVersion(productVersion);
            order.setCustomerVersion(customerVersion);

            long id = orderDao.insert(order);
            order.setId(id);
            selectedItem.postValue(order);
        });
    }

    public void loadCustomerForOrder(long customerId, long customerVersion) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Customer customer = getCustomerForOrder(customerId, customerVersion);
            customerForOrder.postValue(customer);
        });
    }

    private Customer getCustomerForOrder(long customerId, long customerVersion) {
        Customer currentCustomer = customerDao.getCustomerById(customerId);

        if (currentCustomer != null && currentCustomer.getVersion() == customerVersion) {
            return currentCustomer;
        } else {
            CustomerHistory historicalCustomer = customerDao.getCustomerHistoryByIdAndVersion(customerId, customerVersion);
            if (historicalCustomer != null) {
                return historicalCustomer.getCustomer();
            }
        }

        return null;
    }


    public LiveData<Customer> getCustomerForOrder() {
        return customerForOrder;
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<List<Customer>> getCustomers() {
        return customers;
    }

}
