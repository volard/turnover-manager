package com.liberty.turnovermanagement.orders.details;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.base.Constants;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.details.CustomerDetailsActivity;
import com.liberty.turnovermanagement.databinding.ActivityDetailsOrderBinding;
import com.liberty.turnovermanagement.orders.create_update_details.OrderEditActivity;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;
import com.liberty.turnovermanagement.products.details.ProductDetailsActivity;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    private Calendar calendar;
    private ActivityDetailsOrderBinding binding;
    protected OrderDetailsViewModel viewModel;
    protected long itemId = Constants.UNINITIALIZED_INDICATOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);

        calendar = Calendar.getInstance();

        itemId = getIntent().getLongExtra(Constants.ITEM_ID, Constants.UNINITIALIZED_INDICATOR);
        viewModel.loadItem(itemId);
        viewModel.getSelectedItem().observe(this, this::updateUI);

        viewModel.getCustomerForOrder().observe(this, this::updateCustomerUI);
        viewModel.getProductForOrder().observe(this, this::updateProductUI);

        binding.buttonEdit.setOnClickListener(v -> openEditActivity());
        binding.buttonDelete.setOnClickListener(v -> deleteItem());

        setupCardClickListeners();
    }


    private void openEditActivity() {
        Order currentOrder = viewModel.getSelectedItem().getValue();
        if (currentOrder != null) {
            Intent intent = new Intent(this, OrderEditActivity.class);
            intent.putExtra(Constants.ITEM_ID, currentOrder.getId());
            startActivity(intent);
        }
    }

    private void deleteItem() {
        Order item = viewModel.getSelectedItem().getValue();
        if (item != null) {
            viewModel.softDelete(item);
            setResult(Activity.RESULT_OK);
            finish();
        }
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


    private void updateSelectedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDateTime = sdf.format(calendar.getTime());
        binding.tvSelectedDateTime.setText("Selected: " + formattedDateTime);
    }

}

