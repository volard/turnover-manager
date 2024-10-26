package com.liberty.turnovermanagement.orders.details;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.base.Constants;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.details.CustomerDetailsActivity;
import com.liberty.turnovermanagement.databinding.ActivityDetailsOrderBinding;
import com.liberty.turnovermanagement.orders.create_update_details.CustomerSpinnerAdapter;
import com.liberty.turnovermanagement.orders.create_update_details.ProductSpinnerAdapter;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;
import com.liberty.turnovermanagement.products.details.ProductDetailsActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderDetailsActivity extends BaseDetailsActivity<Order, OrderDetailsViewModel, ActivityDetailsOrderBinding> {

    private Product selectedProduct;

    private Customer selectedCustomer;

    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        calendar = Calendar.getInstance();
        binding.btnDateTimePicker.setOnClickListener(v -> showDatePickerDialog());


        viewModel.getProducts().observe(this, this::setupProductSpinner);
        viewModel.getCustomers().observe(this, this::setupCustomerSpinner);

        viewModel.getCustomerForOrder().observe(this, this::updateCustomerUI);
        viewModel.getProductForOrder().observe(this, this::updateProductUI);

        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        binding.customerInfoCard.setOnClickListener(v -> {
            Order currentOrder = viewModel.getSelectedItem().getValue();
            if (currentOrder != null) {
                openCustomerDetails(currentOrder.getCustomerId());
            }
        });

        binding.productInfoCard.setOnClickListener(v -> {
            Order currentOrder = viewModel.getSelectedItem().getValue();
            if (currentOrder != null) {
                openProductDetails(currentOrder.getProductId());
            }
        });
    }

    private void openCustomerDetails(long customerId) {
        Intent intent = new Intent(this, CustomerDetailsActivity.class);
        intent.putExtra(Constants.ITEM_ID, customerId);
        startActivity(intent);
    }

    private void openProductDetails(long productId) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(Constants.ITEM_ID, productId);
        startActivity(intent);
    }

    @Override
    protected ActivityDetailsOrderBinding inflateBinding(LayoutInflater inflater) {
        return ActivityDetailsOrderBinding.inflate(inflater);
    }

    @Override
    protected Class<OrderDetailsViewModel> getViewModelClass() {
        return OrderDetailsViewModel.class;
    }


    @Override
    protected void updateUI(Order order) {
        if (order == null) { return; }

        binding.editTextCity.setText(order.getCity());
        binding.editTextStreet.setText(order.getStreet());
        binding.editTextHome.setText(order.getHome());
        binding.editTextAmount.setText(String.valueOf(order.getAmount()));
        calendar.setTime(java.util.Date.from(order.getDatetime().atZone(ZoneId.systemDefault()).toInstant()));
        updateSelectedDateTime();

        binding.buttonDelete.setVisibility(View.VISIBLE);

        binding.customerInfoCard.setVisibility(View.VISIBLE);
        viewModel.loadCustomerForOrder(order.getCustomerId(), order.getCustomerVersion());

        binding.productInfoCard.setVisibility(View.VISIBLE);
        viewModel.loadProductForOrder(order.getProductId(), order.getProductVersion());
    }

    @Override
    protected void setupButtons() {
        binding.buttonSave.setOnClickListener(v -> saveOrUpdateItem());
        binding.buttonDelete.setOnClickListener(v -> deleteItem());
    }

    private void updateProductUI(Product product) {
        if (product != null) {
            binding.productNameTextView.setText(getString(R.string.product_name_format, product.getName()));
            binding.productAmountTextView.setText(getString(R.string.product_amount_format, product.getAmount()));
            binding.productPriceTextView.setText(getString(R.string.product_price_format, product.getPrice()));
        } else {
            binding.productNameTextView.setText(R.string.product_not_found);
            binding.productAmountTextView.setText("");
            binding.productPriceTextView.setText("");
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    showTimePickerDialog();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    updateSelectedDateTime();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void updateSelectedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDateTime = sdf.format(calendar.getTime());
        binding.tvSelectedDateTime.setText("Selected: " + formattedDateTime);
    }

    private void updateCustomerUI(Customer customer) {
        if (customer != null) {
            binding.customerNameTextView.setText(getString(R.string.customer_name_format,
                    customer.getSurname(), customer.getName(), customer.getMiddleName()));
            binding.customerPhoneTextView.setText(getString(R.string.customer_phone_format, customer.getPhone()));
            binding.customerEmailTextView.setText(getString(R.string.customer_email_format, customer.getEmail()));
        } else {
            binding.customerNameTextView.setText(R.string.customer_not_found);
            binding.customerPhoneTextView.setText("");
            binding.customerEmailTextView.setText("");
        }
    }

    private void setupProductSpinner(List<Product> products) {
        ProductSpinnerAdapter adapter = new ProductSpinnerAdapter(this, products);
        binding.spinnerProducts.setAdapter(adapter);


        binding.spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProduct = (Product) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupCustomerSpinner(List<Customer> customers) {
        CustomerSpinnerAdapter adapter = new CustomerSpinnerAdapter(this, customers);
        binding.spinnerCustomers.setAdapter(adapter);

        binding.spinnerCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCustomer = (Customer) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }


    @Override
    protected Order getItemToSaveOrUpdate() {
        Order order = viewModel.getSelectedItem().getValue();
        if (order == null) {
            order = new Order();
        }

        // Update order fields
        order.setCity(binding.editTextCity.getText().toString());
        order.setStreet(binding.editTextStreet.getText().toString());
        order.setHome(binding.editTextHome.getText().toString());
        order.setAmount(Integer.parseInt(binding.editTextAmount.getText().toString()));
        order.setDatetime(LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()));

        if (selectedProduct.getId() != order.productId || selectedProduct.getId() == Constants.UNINITIALIZED_INDICATOR){
            order.setProductId(selectedProduct.getId());
        }
        if (selectedCustomer.getId() != order.customerId || selectedCustomer.getId() == Constants.UNINITIALIZED_INDICATOR){
            order.setCustomerId(selectedCustomer.getId());
        }

        return order;
    }


}

