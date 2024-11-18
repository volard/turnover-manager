package com.liberty.turnovermanagement.orders.details;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.base.details.BaseDetailsViewModel;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.customers.data.CustomerRepository;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.data.OrderDao;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.products.data.ProductHistory;
import com.liberty.turnovermanagement.products.data.ProductRepository;

import java.util.List;

public class OrderDetailsViewModel extends BaseDetailsViewModel<Order, Void> {
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final LiveData<List<Product>> products;
    private final LiveData<List<Customer>> customers;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final MutableLiveData<Customer> customerForOrder = new MutableLiveData<>();
    private final MutableLiveData<Product> productForOrder = new MutableLiveData<>();

    public OrderDetailsViewModel(Application application) {
        super(application);
        orderDao = db.orderDao();
        productDao = db.productDao();
        customerDao = db.customerDao();
        products = productDao.getAll();
        customers = customerDao.getAll();
        customerRepository = new CustomerRepository(customerDao);
        productRepository = new ProductRepository(productDao);
    }

    public LiveData<Product> getProductForOrder() {
        return productForOrder;
    }

    public void loadProductForOrder(long productId, long productVersion) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Product product = productRepository.getProductByIdAndVersion(productId, productVersion);
            productForOrder.postValue(product);
        });
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
            Customer customer = customerRepository.getCustomerByIdAndVersion(customerId, customerVersion);
            customerForOrder.postValue(customer);
        });
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
