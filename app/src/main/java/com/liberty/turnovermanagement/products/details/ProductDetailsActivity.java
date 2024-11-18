package com.liberty.turnovermanagement.products.details;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;
import com.liberty.turnovermanagement.databinding.ActivityDetailsProductBinding;
import com.liberty.turnovermanagement.products.data.Product;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProductDetailsActivity extends BaseDetailsActivity<Product, ProductDetailViewModel, ActivityDetailsProductBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.editTextPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used in this case
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.editTextPrice.removeTextChangedListener(this); // Remove to avoid infinite loop

                String priceText = s.toString();
                // русский просто добавляет пробелы, а так определяются запятые и точки, что привычнее и удобнее
                Locale locale = new Locale("en", "EN");
                try {
                    // Format the price as currency
                    NumberFormat currencyFormat = NumberFormat.getInstance(locale);
                    Number price = currencyFormat.parse(priceText); // Parse the text
                    String formattedPrice = currencyFormat.format(price); // Format as currency

                    binding.editTextPrice.setText(formattedPrice); // Update the EditText
                    binding.editTextPrice.setSelection(formattedPrice.length()); // Set cursor position
                } catch (ParseException e) {
                    // Handle parsing errors
                    Log.e("ProductDetails", "Error parsing price", e);
                }

                binding.editTextPrice.addTextChangedListener(this); // Re-add the TextWatcher
            }
        });
    }


    @Override
    protected void setupButtons() {
        binding.buttonSave.setOnClickListener(v -> saveOrUpdateItem());
        binding.buttonDelete.setOnClickListener(v -> deleteItem());
        binding.shareButton.setOnClickListener(v -> shareItem());
    }

    private void shareItem() {
        // 1. Get the data to share (e.g., product name, details)
        Product product = viewModel.getSelectedItem().getValue();
        if (product == null) {
            return;
        }
        String productName = product.getName();
        String productDetails = String.valueOf(product.getPrice());
        String shareText = "Check out this product: " + productName + "\n" + productDetails;

        // 2. Create an Intent to share the data
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // 3. Start the sharing activity
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }


    @Override
    protected Class<ProductDetailViewModel> getViewModelClass() {
        return ProductDetailViewModel.class;
    }

    @Override
    protected ActivityDetailsProductBinding inflateBinding(LayoutInflater inflater) {
        return ActivityDetailsProductBinding.inflate(inflater);
    }

    @Override
    protected void updateUI(Product product) {
        if (product == null) {
            return;
        }

        binding.editTextName.setText(product.getName());
        binding.editTextAmount.setText(String.valueOf(product.getAmount()));
        binding.editTextPrice.setText(String.valueOf(product.getPrice()));

        if (product.isDeleted()) {
            binding.editTextName.setEnabled(false);
            binding.editTextAmount.setEnabled(false);
            binding.editTextPrice.setEnabled(false);
            binding.buttonSave.setVisibility(View.GONE);
            binding.labelDeleted.setVisibility(View.VISIBLE);
        } else {
            binding.buttonDelete.setVisibility(View.VISIBLE);
            binding.shareButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Product getItemToSaveOrUpdate() {
        Product product = viewModel.getSelectedItem().getValue();
        if (product == null) {
            product = new Product();
        }

        String name = binding.editTextName.getText().toString().trim();
        String amountStr = binding.editTextAmount.getText().toString().trim();
        String priceStr = binding.editTextPrice.getText().toString().trim();

        // Validation checks
        if (name.isEmpty()) {
            binding.editTextName.setError("Name cannot be empty");
            return null;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
            if (amount < 0) {
                binding.editTextAmount.setError("Amount must be a non-negative integer");
                return null;
            }
        } catch (NumberFormatException e) {
            binding.editTextAmount.setError("Invalid amount");
            return null;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                binding.editTextPrice.setError("Price must be a non-negative number");
                return null;
            }
        } catch (NumberFormatException e) {
            binding.editTextPrice.setError("Invalid price");
            return null;
        }

        // If all validations pass, set the values
        product.setName(name);
        product.setAmount(amount);
        product.setPrice(price);

        return product;
    }
}
