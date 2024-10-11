package com.liberty.turnovermanagement.products;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.liberty.turnovermanagement.R;

public class AddProductActivity extends AppCompatActivity {

    private EditText editTextName, editTextAmount, editTextPrice;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextName = findViewById(R.id.editTextName);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = editTextName.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();

        if (name.isEmpty() || amountStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = Integer.parseInt(amountStr);
        double price = Double.parseDouble(priceStr);

        Product newProduct = new Product(name, amount, price);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("product", newProduct);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

