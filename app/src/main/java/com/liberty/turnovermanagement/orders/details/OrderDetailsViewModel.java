package com.liberty.turnovermanagement.orders.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
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

import java.util.List;

public class OrderDetailsViewModel extends AndroidViewModel {
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final MutableLiveData<Order> selectedOrder = new MutableLiveData<>();
    private final LiveData<List<Product>> products;
    private final LiveData<List<Customer>> customers;

    private final MutableLiveData<Customer> customerForOrder = new MutableLiveData<>();

    public OrderDetailsViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        orderDao       = db.orderDao();
        productDao     = db.productDao();
        customerDao    = db.customerDao();
        products       = productDao.getAll();
        customers      = customerDao.getAll();
    }

    public void loadOrder(long orderId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Order order = orderDao.getOrderById(orderId);
            selectedOrder.postValue(order);
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
                return convertToCustomer(historicalCustomer);
            }
        }

        return null; // Customer not found
    }


    private Customer convertToCustomer(CustomerHistory history) {
        Customer customer = new Customer();
        customer.setId(history.getCustomerId());
        customer.setSurname(history.getSurname());
        customer.setName(history.getName());
        customer.setMiddleName(history.getMiddleName());
        customer.setPhone(history.getPhone());
        customer.setEmail(history.getEmail());
        customer.setVersion(history.getVersion());
        // Set other fields as necessary
        return customer;
    }

    public LiveData<Customer> getCustomerForOrder() {
        return customerForOrder;
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
            long productVersion = productDao.getProductVersion(order.getProductId());
            long customerVersion = customerDao.getCustomerVersion(order.getCustomerId());

            order.setProductVersion(productVersion);
            order.setCustomerVersion(customerVersion);

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
