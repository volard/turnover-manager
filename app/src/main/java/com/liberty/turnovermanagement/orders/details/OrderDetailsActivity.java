package com.liberty.turnovermanagement.orders.details;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.customers.Customer;
import com.liberty.turnovermanagement.orders.list.OrdersViewModel;
import com.liberty.turnovermanagement.orders.model.Order;
import com.liberty.turnovermanagement.orders.model.OrderWithDetails;
import com.liberty.turnovermanagement.products.Product;
import com.liberty.turnovermanagement.products.ProductsViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    private EditText editTextCity, editTextStreet, editTextHome, editTextAmount;
    private Button buttonSave, buttonDelete, btnDateTimePicker;
    private OrderWithDetails existingOrder;
    private TextView tvSelectedDateTime;
    private Product selectedProduct;
    private Customer selectedCustomer;
    private OrdersViewModel viewModel;
    private Calendar calendar;
    private Spinner spinnerProducts, spinnerCustomers;


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
        tvSelectedDateTime.setText("Selected: " + formattedDateTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_order);

        viewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        editTextCity       = findViewById(R.id.editTextCity);
        editTextAmount     = findViewById(R.id.editTextAmount);
        editTextStreet     = findViewById(R.id.editTextStreet);
        editTextHome       = findViewById(R.id.editTextHome);
        buttonSave         = findViewById(R.id.buttonSave);
        buttonDelete       = findViewById(R.id.buttonDelete);
        btnDateTimePicker  = findViewById(R.id.btnDateTimePicker);
        tvSelectedDateTime = findViewById(R.id.tvSelectedDateTime);
        spinnerProducts    = findViewById(R.id.spinnerProducts);
        spinnerCustomers   = findViewById(R.id.spinnerCustomers);

        calendar = Calendar.getInstance();
        btnDateTimePicker.setOnClickListener(v -> showDatePickerDialog());
        setupProductSpinner();
        setupCustomerSpinner();

        existingOrder = (OrderWithDetails) getIntent().getSerializableExtra("order");
        if (existingOrder != null) {
            // Pre-fill fields if editing an existing product
            editTextCity.setText(existingOrder.order.getCity());
            editTextStreet.setText(String.valueOf(existingOrder.order.getStreet()));
            editTextHome.setText(String.valueOf(existingOrder.order.getHome()));
            editTextAmount.setText(String.valueOf(existingOrder.order.getAmount()));

            buttonDelete.setVisibility(View.VISIBLE);
        }

        buttonSave.setOnClickListener(v -> saveOrder());
        buttonDelete.setOnClickListener(v -> deleteOrder());
    }

    private void setupProductSpinner() {
        List<Product> products = viewModel.getProducts();
        ProductSpinnerAdapter adapter = new ProductSpinnerAdapter(this, products);
        spinnerProducts.setAdapter(adapter);

        // Set a listener for item selection if needed
        spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProduct = (Product) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ...
            }
        });
        if (existingOrder != null){
            int positionToSelect = -1;
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).getId() == existingOrder.product.getId()) {
                    positionToSelect = i;
                    break;
                }
            }
            assert positionToSelect != -1; // if so, its a bug
            spinnerProducts.setSelection(positionToSelect);
        }
        else {
            spinnerProducts.setSelection(0);
        }
    }

    private void setupCustomerSpinner() {
//        List<Customer> customers = viewModel.getCustomers();
//        CustomerSpinnerAdapter adapter = new CustomerSpinnerAdapter(this, customers);
//        spinnerProducts.setAdapter(adapter);
//
//        // Set a listener for item selection if needed
//        spinnerCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedCustomer = (Customer) parent.getItemAtPosition(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // ...
//            }
//        });
//        if (existingOrder != null){
//            int positionToSelect = -1;
//            for (int i = 0; i < adapter.getCount(); i++) {
//                if (adapter.getItem(i).getId() == existingOrder.customer.getId()) {
//                    positionToSelect = i;
//                    break;
//                }
//            }
//            assert positionToSelect != -1; // if so, its a bug
//            spinnerCustomers.setSelection(positionToSelect);
//        }
//        else {
//            spinnerCustomers.setSelection(0);
//        }
    }

    private void deleteOrder() {
        if (existingOrder != null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Order")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("order", existingOrder);
                    resultIntent.putExtra("delete", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void saveOrder() {
        String city = editTextCity.getText().toString().trim();
        String street = editTextStreet.getText().toString().trim();
        String home = editTextHome.getText().toString().trim();
        int amount = Integer.parseInt(editTextAmount.getText().toString());
        LocalDateTime dateTime = calendar.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Intent resultIntent = new Intent();

        if (existingOrder != null) {
            // Update existing product
            existingOrder.order.setCity(city);
            existingOrder.order.setAmount(amount);
            existingOrder.order.setStreet(street);
            existingOrder.order.setHome(home);
            existingOrder.order.setProductId(selectedProduct.getId());
            existingOrder.order.setCustomerId(selectedCustomer.getId());
            viewModel.update(existingOrder.order);
        } else {
            // Create new order and dependencies
            Order order = new Order(selectedProduct.getId(), amount, selectedCustomer.getId(),
                    dateTime, city, street, home);
            resultIntent.putExtra("order", order);
        }

        resultIntent.putExtra("isNewOrder", existingOrder == null);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

