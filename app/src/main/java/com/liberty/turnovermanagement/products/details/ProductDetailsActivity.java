package com.liberty.turnovermanagement.products.details;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.AppDatabase;
import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.products.data.Product;

public class ProductDetailsActivity extends AppCompatActivity {

    private EditText editTextName, editTextAmount, editTextPrice;
    private Button buttonSave;
    private Product existingProduct;
    private Button buttonDelete;
    private TextView labelDeleted;
    private ProductDetailViewModel viewModel;
    private long productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);

        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        productId = getIntent().getLongExtra("productId", -1);

        editTextName   = findViewById(R.id.editTextName);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextPrice  = findViewById(R.id.editTextPrice);
        buttonSave     = findViewById(R.id.buttonSave);
        buttonDelete   = findViewById(R.id.buttonDelete);
        labelDeleted   = findViewById(R.id.labelDeleted);

        if (productId != -1) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                Product product = AppDatabase.getDatabase(this).productDao().getProductById(productId);
                // LiveData is designed to be updated on the main thread to ensure thread safety and proper UI updates
                runOnUiThread(() -> viewModel.setSelectedProduct(product));
            });
        }

        viewModel.getSelectedProduct().observe(this, this::updateUI);

        existingProduct = (Product) getIntent().getSerializableExtra("product");
//        if (existingProduct != null) {
//            // Pre-fill fields if editing an existing product
//            editTextName.setText(existingProduct.getName());
//            editTextAmount.setText(String.valueOf(existingProduct.getAmount()));
//            editTextPrice.setText(String.valueOf(existingProduct.getPrice()));
//
//            if (existingProduct.isDeleted()){
//                editTextName.setEnabled(false);
//                editTextAmount.setEnabled(false);
//                editTextPrice.setEnabled(false);
//
//                buttonSave.setVisibility(View.GONE);
//                labelDeleted.setVisibility(View.VISIBLE);
//            }
//            else{
//                // Show delete button only for existing products
//                buttonDelete.setVisibility(View.VISIBLE);
//            }
//        }

        buttonSave.setOnClickListener(v -> saveProduct());
        buttonDelete.setOnClickListener(v -> deleteProduct());
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
            editTextName.setText(product.getName());
            editTextAmount.setText(String.valueOf(product.getAmount()));
            editTextPrice.setText(String.valueOf(product.getPrice()));

            if (product.isDeleted()) {
                // Disable editing for deleted products
                editTextName.setEnabled(false);
                editTextAmount.setEnabled(false);
                editTextPrice.setEnabled(false);
                buttonSave.setVisibility(View.GONE);
                labelDeleted.setVisibility(View.VISIBLE);
            } else {
                buttonDelete.setVisibility(View.VISIBLE);
            }
        }
    }

    private void saveProduct() {
        Product product = viewModel.getSelectedProduct().getValue();
        if (product == null) {
            product = new Product();
        }

        // Update product fields from UI elements
        product.setName(editTextName.getText().toString());
        product.setAmount(Integer.parseInt(editTextAmount.getText().toString()));
        product.setPrice(Double.parseDouble(editTextPrice.getText().toString()));

        if (productId == -1) {
            viewModel.addNewProduct(product);
        } else {
            viewModel.updateProduct(product);
        }

        setResult(Activity.RESULT_OK);
        finish();
    }
}

