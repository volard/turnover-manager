package com.liberty.turnovermanagement.orders.details;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.base.Constants;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.ActivityDetailsOrderBinding;
import com.liberty.turnovermanagement.orders.create_update_details.OrderEditActivity;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.products.data.Product;

public class OrderDetailsActivity extends AppCompatActivity {

    private ActivityDetailsOrderBinding binding;
    protected OrderDetailsViewModel viewModel;
    protected long itemId = Constants.UNINITIALIZED_INDICATOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);

        itemId = getIntent().getLongExtra(Constants.ITEM_ID, Constants.UNINITIALIZED_INDICATOR);
        viewModel.loadItem(itemId);

        viewModel.getSelectedItem().observe(this, this::updateUI);

        viewModel.getCustomerForOrder().observe(this, this::updateCustomerUI);
        viewModel.getProductForOrder().observe(this, this::updateProductUI);

        binding.buttonEdit.setOnClickListener(v -> openEditActivity());
        binding.buttonDelete.setOnClickListener(v -> deleteItem());
        binding.shareButton.setOnClickListener(v -> shareItem());
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


    private void shareItem() {
        Order order = viewModel.getSelectedItem().getValue();
        if (order == null){
            return;
        }
        String shareText = String.format("Check out this order at city: %s", order.getCity());

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }


    protected void updateUI(Order order) {
        if (order == null) {
            return;
        }

        // Update address info card
        binding.cityTextView.setText("City: " + order.getCity());
        binding.streetTextView.setText("Street: " + order.getStreet());
        binding.homeTextView.setText("Home: " + order.getHome());

        // Make the address info card visible
        binding.addressInfoCard.setVisibility(View.VISIBLE);

        binding.shareButton.setVisibility(View.VISIBLE);

        // Update date time
        binding.tvSelectedDateTime.setText("Selected: " + order.getCreatedAt().format(Constants.DATE_TIME_FORMATTER));

        binding.buttonDelete.setVisibility(View.VISIBLE);

        viewModel.loadCustomerForOrder(order.getCustomerId(), order.getCustomerVersion());
        binding.customerInfoCard.setVisibility(View.VISIBLE);

        viewModel.loadProductForOrder(order.getProductId(), order.getProductVersion());
        binding.productInfoCard.setVisibility(View.VISIBLE);
    }

    private void updateProductUI(Product product) {
        if (product != null) {
            binding.productNameTextView.setText(getString(R.string.product_name_format, product.getName()));
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
}

