package com.liberty.turnovermanagement.orders.create_update_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.base.Constants;
import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.databinding.ActivityDetailsOrderBinding;
import com.liberty.turnovermanagement.databinding.ActivityEditOrderBinding;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductHistory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderEditActivity extends BaseDetailsActivity<Order, OrderEditViewModel, ActivityEditOrderBinding> {

    private Spinner productSpinner;
    private Spinner productVersionSpinner;
    private Spinner customerSpinner;
    private Spinner customerVersionSpinner;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();

        setupSpinners();
        setupObservers();
    }

    @Override
    protected void setupButtons() {
        binding.buttonSave.setOnClickListener(v -> saveOrUpdateItem());
    }

    @Override
    protected Class<OrderEditViewModel> getViewModelClass() {
        return OrderEditViewModel.class;
    }

    @Override
    protected ActivityEditOrderBinding inflateBinding(LayoutInflater inflater) {
        return ActivityEditOrderBinding.inflate(inflater);
    }

    @Override
    protected void updateUI(Order order) {
        if (order == null) {
            return;
        }

        binding.editTextCity.setText(order.getCity());
        binding.editTextStreet.setText(order.getStreet());
        binding.editTextHome.setText(order.getHome());
        binding.editTextAmount.setText(String.valueOf(order.getAmount()));

        // Set the date and time
        calendar.setTime(java.util.Date.from(order.getDatetime().atZone(ZoneId.systemDefault()).toInstant()));
        updateSelectedDateTime();

        // Show delete button for existing orders
        binding.buttonDelete.setVisibility(View.VISIBLE);

        // Update product spinner
        updateProductSpinner(order.getProductId());

        // Update customer spinner
        updateCustomerSpinner(order.getCustomerId());

        // Load product versions
        viewModel.loadProductVersions(order.getProductId());

        // Load customer versions
        viewModel.loadCustomerVersions(order.getCustomerId());
    }


    private void updateProductSpinner(long productId) {
        viewModel.getProducts().observe(this, products -> {
            if (products != null) {
                int selectedPosition = -1;
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getId() == productId) {
                        selectedPosition = i;
                        break;
                    }
                }
                if (selectedPosition != -1) {
                    productSpinner.setSelection(selectedPosition);
                }
            }
        });
    }

    private void updateCustomerSpinner(long customerId) {
        viewModel.getCustomers().observe(this, customers -> {
            if (customers != null) {
                int selectedPosition = -1;
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getId() == customerId) {
                        selectedPosition = i;
                        break;
                    }
                }
                if (selectedPosition != -1) {
                    customerSpinner.setSelection(selectedPosition);
                }
            }
        });
    }


    private void updateSelectedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDateTime = sdf.format(calendar.getTime());
        binding.tvSelectedDateTime.setText("Selected: " + formattedDateTime);
    }


    @Override
    protected Order getItemToSaveOrUpdate() {
        Order order = viewModel.getSelectedItem().getValue();
        if (order == null) {
            order = new Order();
        }

        // Update order fields
        order.setCity(binding.editTextCity.getText().toString().trim());
        order.setStreet(binding.editTextStreet.getText().toString().trim());
        order.setHome(binding.editTextHome.getText().toString().trim());
        order.setAmount(Integer.parseInt(binding.editTextAmount.getText().toString().trim()));
        order.setDatetime(LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()));

        // Get selected product from spinner
        Product selectedProduct = (Product) productSpinner.getSelectedItem();
        if (selectedProduct != null) {
            order.setProductId(selectedProduct.getId());
        }

        // Get selected customer from spinner
        Customer selectedCustomer = (Customer) customerSpinner.getSelectedItem();
        if (selectedCustomer != null) {
            order.setCustomerId(selectedCustomer.getId());
        }

        // Handle product version if available
        if (productVersionSpinner.getVisibility() == View.VISIBLE) {
            ProductHistory selectedProductVersion = (ProductHistory) productVersionSpinner.getSelectedItem();
            if (selectedProductVersion != null) {
                order.setProductVersion(selectedProductVersion.getVersion());
            }
        }

        // Handle customer version if available
        if (customerVersionSpinner.getVisibility() == View.VISIBLE) {
            CustomerHistory selectedCustomerVersion = (CustomerHistory) customerVersionSpinner.getSelectedItem();
            if (selectedCustomerVersion != null) {
                order.setCustomerVersion(selectedCustomerVersion.getVersion());
            }
        }

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
