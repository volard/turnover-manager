package com.liberty.turnovermanagement.orders.details;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.ActivityDetailsOrderBinding;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;

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

        // Update spinners
//        updateProductSpinner(order.getProductId());
//        updateCustomerSpinner(order.getCustomerId());

        binding.buttonDelete.setVisibility(View.VISIBLE);
        binding.customerInfoCard.setVisibility(View.VISIBLE);
       // Load customer data
        viewModel.loadCustomerForOrder(order.getCustomerId(), order.getCustomerVersion());
    }

    @Override
    protected void setupButtons() {
        binding.buttonSave.setOnClickListener(v -> saveOrUpdateItem());
        binding.buttonDelete.setOnClickListener(v -> deleteItem());
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


    /*private void updateProductSpinner(long productId) {
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
    }*/


    private void setupProductSpinner(List<Product> products) {
        ProductSpinnerAdapter adapter = new ProductSpinnerAdapter(this, products);
        binding.spinnerProducts.setAdapter(adapter);
        selectedProduct = products.get(0);

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
        selectedCustomer = customers.get(0);

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
        order.setProductId(selectedProduct.getId());
        order.setCustomerId(selectedCustomer.getId());

        return order;
    }

}

