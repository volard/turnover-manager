package com.liberty.turnovermanagement.orders.create_update_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.databinding.ActivityDetailsOrderBinding;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.details.OrderDetailsViewModel;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductHistory;

import java.util.List;

public class OrderEditActivity extends BaseDetailsActivity<Order, OrderDetailsViewModel, ActivityDetailsOrderBinding> {

    private Spinner productSpinner;
    private Spinner productVersionSpinner;
    private Spinner customerSpinner;
    private Spinner customerVersionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSpinners();
        setupObservers();
    }

    @Override
    protected Class<OrderDetailsViewModel> getViewModelClass() {
        return OrderDetailsViewModel.class;
    }

    @Override
    protected ActivityDetailsOrderBinding inflateBinding(LayoutInflater inflater) {
        return ActivityDetailsOrderBinding.inflate(inflater);
    }

    @Override
    protected void updateUI(Order order) {
        if (order == null) return;

        // Update other UI elements with order details
        // ...

        viewModel.loadProductsForSpinner();
        viewModel.loadCustomersForSpinner();
    }

    @Override
    protected Order getItemToSaveOrUpdate() {
        Order order = viewModel.getSelectedItem().getValue();
        if (order == null) {
            order = new Order();
        }

        // Update order fields from UI elements
        // ...

        return order;
    }

    private void setupSpinners() {
        productSpinner = findViewById(R.id.product_spinner);
        productVersionSpinner = findViewById(R.id.product_version_spinner);
        customerSpinner = findViewById(R.id.customer_spinner);
        customerVersionSpinner = findViewById(R.id.customer_version_spinner);

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = (Product) parent.getItemAtPosition(position);
                viewModel.loadProductVersions(selectedProduct.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                viewModel.loadCustomerVersions(selectedCustomer.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupObservers() {
        viewModel.getProducts().observe(this, this::updateProductSpinner);
        viewModel.getProductVersions().observe(this, this::updateProductVersionSpinner);
        viewModel.getCustomers().observe(this, this::updateCustomerSpinner);
        viewModel.getCustomerVersions().observe(this, this::updateCustomerVersionSpinner);
    }

    private void updateProductSpinner(List<Product> products) {
        ArrayAdapter<Product> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, products);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(adapter);
    }

    private void updateProductVersionSpinner(List<ProductHistory> productVersions) {
        if (productVersions.isEmpty()) {
            productVersionSpinner.setVisibility(View.GONE);
        } else {
            productVersionSpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<ProductHistory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productVersions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            productVersionSpinner.setAdapter(adapter);
        }
    }

    private void updateCustomerSpinner(List<Customer> customers) {
        ArrayAdapter<Customer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpinner.setAdapter(adapter);
    }

    private void updateCustomerVersionSpinner(List<CustomerHistory> customerVersions) {
        if (customerVersions.isEmpty()) {
            customerVersionSpinner.setVisibility(View.GONE);
        } else {
            customerVersionSpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CustomerHistory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customerVersions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            customerVersionSpinner.setAdapter(adapter);
        }
    }
}
