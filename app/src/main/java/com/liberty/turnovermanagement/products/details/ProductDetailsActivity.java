package com.liberty.turnovermanagement.products.details;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.liberty.turnovermanagement.databinding.ActivityDetailsProductBinding;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;

public class ProductDetailsActivity extends BaseDetailsActivity<Product, ProductDetailViewModel, ActivityDetailsProductBinding> {

    @Override
    protected void setupButtons() {
        binding.buttonSave.setOnClickListener(v -> saveOrUpdateItem());
        binding.buttonDelete.setOnClickListener(v -> deleteItem());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (product == null) { return; }

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
