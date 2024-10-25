package com.liberty.turnovermanagement.products.details;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.databinding.ActivityDetailsProductBinding;
import com.liberty.turnovermanagement.products.data.Product;

public class ProductDetailsActivity extends AppCompatActivity {


    private ActivityDetailsProductBinding binding;
    private ProductDetailViewModel viewModel;
    private long productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        productId = getIntent().getLongExtra("productId", -1);


        if (productId != -1) {
            viewModel.loadProduct(productId);
        }

        viewModel.getSelectedProduct().observe(this, this::updateUI);

        binding.buttonSave.setOnClickListener(v -> saveProduct());
        binding.buttonDelete.setOnClickListener(v -> deleteProduct());
    }

    private void setupVersionHistory() {
        viewModel.getProductHistory(productId).observe(this, history -> {
            // Display the version history, e.g., in a RecyclerView
            // You'll need to create a new adapter and layout for this
        });
    }

    private void deleteProduct() {
        Product product = viewModel.getSelectedProduct().getValue();
        if (product != null) {
            viewModel.softDelete(product);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void updateUI(Product product) {
        if (product != null) {
            binding.editTextName.setText(product.getName());
            binding.editTextAmount.setText(String.valueOf(product.getAmount()));
            binding.editTextPrice.setText(String.valueOf(product.getPrice()));

            if (product.isDeleted()) {
                // Disable editing for deleted products
                binding.editTextName.setEnabled(false);
                binding.editTextAmount.setEnabled(false);
                binding.editTextPrice.setEnabled(false);
                binding.buttonSave.setVisibility(View.GONE);
                binding.labelDeleted.setVisibility(View.VISIBLE);
            } else {
                binding.buttonDelete.setVisibility(View.VISIBLE);
            }
        }
    }

    private void saveProduct() {
        Product product = viewModel.getSelectedProduct().getValue();
        if (product == null) {
            product = new Product();
        }

        // Update product fields from UI elements
        product.setName(binding.editTextName.getText().toString());
        product.setAmount(Integer.parseInt(binding.editTextAmount.getText().toString()));
        product.setPrice(Double.parseDouble(binding.editTextPrice.getText().toString()));

        if (productId == -1) {
            viewModel.addNewProduct(product);
        } else {
            viewModel.updateProduct(product);
        }

        setResult(Activity.RESULT_OK);
        finish();
    }
}

