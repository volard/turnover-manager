package com.liberty.turnovermanagement.orders.details;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.ActivityDetailsOrderBinding;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.products.data.Product;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    private Product selectedProduct;
    private Customer selectedCustomer;
    private OrderDetailsViewModel viewModel;
    private Calendar calendar;
    private ActivityDetailsOrderBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);

        calendar = Calendar.getInstance();
        binding.btnDateTimePicker.setOnClickListener(v -> showDatePickerDialog());


        long orderId = getIntent().getLongExtra("orderId", -1);
        if (orderId != -1) {
            viewModel.loadOrder(orderId);
        }

        viewModel.getProducts().observe(this, this::setupProductSpinner);
        viewModel.getCustomers().observe(this, this::setupCustomerSpinner);

        viewModel.getSelectedOrder().observe(this, this::updateUI);
        viewModel.getCustomerForOrder().observe(this, this::updateCustomerUI);

        binding.buttonSave.setOnClickListener(v -> saveOrder());
        binding.buttonDelete.setOnClickListener(v -> deleteOrder());
    }

    private void updateUI(Order order) {
        if (order == null) { return; }

        binding.editTextCity.setText(order.getCity());
        binding.editTextStreet.setText(order.getStreet());
        binding.editTextHome.setText(order.getHome());
        binding.editTextAmount.setText(String.valueOf(order.getAmount()));
        calendar.setTime(java.util.Date.from(order.getDatetime().atZone(ZoneId.systemDefault()).toInstant()));
        updateSelectedDateTime();

        // Update spinners
        updateProductSpinner(order.getProductId());
        updateCustomerSpinner(order.getCustomerId());

        binding.buttonDelete.setVisibility(View.VISIBLE);
        binding.customerInfoCard.setVisibility(View.VISIBLE);
       // Load customer data
        viewModel.loadCustomerForOrder(order.getCustomerId(), order.getCustomerVersion());
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


    private void updateProductSpinner(long productId) {
        ProductSpinnerAdapter adapter = (ProductSpinnerAdapter) binding.spinnerProducts.getAdapter();
        if (adapter == null) { return; }

        for (int i = 0; i < adapter.getCount(); i++) {
            Product product = adapter.getItem(i);
            if (product != null && product.getId() == productId) {
                binding.spinnerProducts.setSelection(i);
                selectedProduct = product;
                break;
            }
        }
    }

    private void updateCustomerSpinner(long customerId) {
        CustomerSpinnerAdapter adapter = (CustomerSpinnerAdapter) binding.spinnerCustomers.getAdapter();
        if (adapter == null) { return; }

        for (int i = 0; i < adapter.getCount(); i++) {
            Customer customer = adapter.getItem(i);
            if (customer != null && customer.getId() == customerId) {
                binding.spinnerCustomers.setSelection(i);
                selectedCustomer = customer;
                break;
            }
        }
    }


    private void setupProductSpinner(List<Product> products) {
        ProductSpinnerAdapter adapter = new ProductSpinnerAdapter(this, products);
        binding.spinnerProducts.setAdapter(adapter);

        binding.spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = (Product) parent.getItemAtPosition(position);
                viewModel.updateSelectedOrderProduct(selectedProduct);
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
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                viewModel.updateSelectedOrderCustomer(selectedCustomer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void deleteOrder() {
        Order order = viewModel.getSelectedOrder().getValue();
        if (order != null) {
            viewModel.deleteOrder(order);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void saveOrder() {
        Order order = viewModel.getSelectedOrder().getValue();
        if (order == null) {
            order = new Order();
        }

        // Update order fields
        order.setCity(binding.editTextCity.getText().toString());
        order.setStreet(binding.editTextStreet.getText().toString());
        order.setHome(binding.editTextHome.getText().toString());
        order.setAmount(Integer.parseInt(binding.editTextAmount.getText().toString()));
        order.setDatetime(LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()));
        order.setProductId(selectedProduct.getId());
        order.setCustomerId(selectedCustomer.getId());

        if (order.getId() == 0) {
            viewModel.addNewOrder(order);
        } else {
            viewModel.updateOrder(order);
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

}

