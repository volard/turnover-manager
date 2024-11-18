package com.liberty.turnovermanagement.orders.create_update_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.liberty.turnovermanagement.base.Constants;
import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.databinding.ActivityEditOrderBinding;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductHistory;

import java.time.LocalDateTime;
import java.util.List;

public class OrderEditActivity extends BaseDetailsActivity<Order, OrderEditViewModel, ActivityEditOrderBinding> {
    private long selectedCustomerId = Constants.UNINITIALIZED_INDICATOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSpinners();
        setupObservers();
    }

    private void setupObservers() {
        viewModel.getSelectedItem().observe(this, order -> {
            updateProductSpinner(order.getProductId());
            updateCustomerSpinner(order.getCustomerId());
        });

        viewModel.getSelectedItem().observe(this, order -> {
            viewModel.loadCustomerVersions(order.getCustomerId());
        });

        viewModel.getSelectedProduct().observe(this, product -> {
            viewModel.loadProductVersions(viewModel.getSelectedItem().getValue().getProductId());
        });

        viewModel.getProducts().observe(this, this::updateProductSpinner);
        viewModel.getProductVersions().observe(this, this::updateProductVersionSpinner);
        viewModel.getCustomers().observe(this, this::updateCustomerSpinner);
        viewModel.getCustomerVersions().observe(this, this::updateCustomerVersionSpinner);
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

        selectedCustomerId = order.getCustomerId();
        binding.editTextCity.setText(order.getCity());
        binding.editTextStreet.setText(order.getStreet());
        binding.editTextHome.setText(order.getHome());
        binding.editTextAmount.setText(String.valueOf(order.getAmount()));
        binding.tvSelectedDateTime.setText(order.getCreatedAt().format(Constants.DATE_TIME_FORMATTER));
    }

    private void updateProductSpinner(long productId) {
        viewModel.getProducts().observe(this, products -> {
            if (products != null) {
                int selectedPosition = (int) Constants.UNINITIALIZED_INDICATOR;
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getId() == productId) {
                        selectedPosition = i;
                        break;
                    }
                }
                if (selectedPosition != Constants.UNINITIALIZED_INDICATOR) {
                    binding.productSpinner.setSelection(selectedPosition);
                }
            }
        });
    }

    private void updateCustomerSpinner(long customerId) {
        viewModel.getCustomers().observe(this, customers -> {
            if (customers != null) {
                int selectedPosition = (int) Constants.UNINITIALIZED_INDICATOR;
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getId() == customerId) {
                        selectedPosition = i;
                        break;
                    }
                }
                if (selectedPosition != Constants.UNINITIALIZED_INDICATOR) {
                    binding.customerSpinner.setSelection(selectedPosition);
                }
            }
        });
    }

    @Override
    protected Order getItemToSaveOrUpdate() {
        Order order = viewModel.getSelectedItem().getValue();
        if (order == null) {
            order = new Order();
        }

        // Validate city
        String city = binding.editTextCity.getText().toString().trim();
        if (city.isEmpty()) {
            binding.editTextCity.setError("City cannot be empty");
            return null;
        }
        order.setCity(city);

        // Validate street
        String street = binding.editTextStreet.getText().toString().trim();
        if (street.isEmpty()) {
            binding.editTextStreet.setError("Street cannot be empty");
            return null;
        }
        order.setStreet(street);

        // Validate home
        String home = binding.editTextHome.getText().toString().trim();
        if (home.isEmpty()) {
            binding.editTextHome.setError("Home cannot be empty");
            return null;
        }
        order.setHome(home);

        // Validate amount
        String amountStr = binding.editTextAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            binding.editTextAmount.setError("Amount cannot be empty");
            return null;
        }


        try {
            int amount = Integer.parseInt(amountStr);
            if (amount <= 0) {
                binding.editTextAmount.setError("Amount must be a positive integer");
                return null;
            }
            order.setAmount(amount);
        } catch (NumberFormatException e) {
            binding.editTextAmount.setError("Invalid amount");
            return null;
        }


        // Validate product selection
        Product selectedProduct = (Product) binding.productSpinner.getSelectedItem();
        if (selectedProduct == null) {
            Toast.makeText(this, "Please select a product", Toast.LENGTH_SHORT).show();
            return null;
        }
        order.setProductId(selectedProduct.getId());

        // Validate customer selection
        Customer selectedCustomer = (Customer) binding.customerSpinner.getSelectedItem();
        if (selectedCustomer == null) {
            Toast.makeText(this, "Please select a customer", Toast.LENGTH_SHORT).show();
            return null;
        }
        order.setCustomerId(selectedCustomer.getId());


        int enteredAmount = Integer.parseInt(binding.editTextAmount.getText().toString());

        // Handle product version if available
        if (binding.productVersionSpinner.getVisibility() == View.VISIBLE) {
            ProductHistory selectedProductVersion = (ProductHistory) binding.productVersionSpinner.getSelectedItem();
            if (selectedProductVersion != null) {
                order.setProductVersion(selectedProductVersion.getVersion());
                if (enteredAmount > selectedProductVersion.getAmount()) {
                    binding.editTextAmount.setError("Amount exceeds available stock");
                    return null;
                }
            } else {
                if (enteredAmount > selectedProduct.getAmount()) {
                    binding.editTextAmount.setError("Amount exceeds available stock");
                    return null;
                }
            }
        }

        // Handle customer version if available
        if (binding.customerVersionSpinner.getVisibility() == View.VISIBLE) {
            CustomerHistory selectedCustomerVersion = (CustomerHistory) binding.customerVersionSpinner.getSelectedItem();
            if (selectedCustomerVersion != null) {
                order.setCustomerVersion(selectedCustomerVersion.getVersion());
            }
        }


        order.setCreatedAt(LocalDateTime.now());

        return order;
    }


    private void setupSpinners() {
        binding.productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = (Product) parent.getItemAtPosition(position);
                int maxQuantity = selectedProduct.getAmount();
                binding.tvMaxQuantityHint.setText("Max: " + maxQuantity); // Visual hint for maximum amount
                viewModel.loadProductVersions(selectedProduct.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.productVersionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductHistory productHistory = (ProductHistory) parent.getItemAtPosition(position);
                int maxQuantity = productHistory.getAmount();
                binding.tvMaxQuantityHint.setText("Max: " + maxQuantity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                selectedCustomerId = selectedCustomer.getId();
                viewModel.loadCustomerVersions(selectedCustomer.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void updateProductSpinner(List<Product> products) {
        ProductSpinnerAdapter adapter = new ProductSpinnerAdapter(this, products);
        binding.productSpinner.setAdapter(adapter);
    }

    private void updateProductVersionSpinner(List<ProductHistory> productVersions) {
        if (productVersions.isEmpty()) {
            binding.productVersionSpinner.setVisibility(View.GONE);
        } else {
            ProductHistorySpinnerAdapter adapter = new ProductHistorySpinnerAdapter(this, productVersions, selectedProduct);
            binding.productVersionSpinner.setAdapter(adapter);

            Order currentOrder = viewModel.getSelectedItem().getValue();
            if (currentOrder != null){
                // everytime customer match that one which in the order, the order's version is set up
                if (currentOrder.getProductId() == selectedProduct.getId()){
                    for (int i = 0; i < productVersions.size(); i++) {
                        if (productVersions.get(i).getVersion() == currentOrder.getProductVersion()) {
                            binding.customerVersionSpinner.setSelection(i);
                            break;
                        }
                    }
                }

            }

            binding.productVersionSpinner.setVisibility(View.VISIBLE);
        }
    }

    private void updateCustomerSpinner(List<Customer> customers) {
        CustomerSpinnerAdapter adapter = new CustomerSpinnerAdapter(this, customers);
        binding.customerSpinner.setAdapter(adapter);
    }

    private void updateCustomerVersionSpinner(List<CustomerHistory> customerVersions) {
        if (customerVersions.isEmpty()) {
            binding.customerVersionSpinner.setVisibility(View.GONE);
        } else {
            CustomerHistorySpinnerAdapter adapter = new CustomerHistorySpinnerAdapter(this, customerVersions);
            binding.customerVersionSpinner.setAdapter(adapter);

            Order currentOrder = viewModel.getSelectedItem().getValue();
            if (currentOrder != null){
                // everytime customer match that one which in the order, the order's version is set up
                if (currentOrder.getCustomerId() == selectedCustomerId){
                    for (int i = 0; i < customerVersions.size(); i++) {
                        if (customerVersions.get(i).getVersion() == currentOrder.getCustomerVersion()) {
                            binding.customerVersionSpinner.setSelection(i);
                            break;
                        }
                    }
                }

            }

            binding.customerVersionSpinner.setVisibility(View.VISIBLE);
        }
    }
}
