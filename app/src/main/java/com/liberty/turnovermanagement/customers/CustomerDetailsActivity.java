package com.liberty.turnovermanagement.customers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.R;

public class CustomerDetailsActivity extends AppCompatActivity {

    private EditText editTextSurName, editTextName, editTextMiddleName, editTextPhone, editTextEmail;
    private Button buttonSave, buttonDelete;
    private TextView labelDeleted;
    private Customer existingCustomer;
    private CustomersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_customer);

        viewModel = new ViewModelProvider(this).get(CustomersViewModel.class);

        editTextName       = findViewById(R.id.editTextName);
        editTextSurName    = findViewById(R.id.editTextSurName);
        editTextMiddleName = findViewById(R.id.editTextMiddleName);
        editTextPhone      = findViewById(R.id.editTextPhone);
        editTextEmail      = findViewById(R.id.editTextEmail);
        buttonSave         = findViewById(R.id.buttonSave);
        buttonDelete       = findViewById(R.id.buttonDelete);
        labelDeleted       = findViewById(R.id.labelDeleted);

        existingCustomer = (Customer) getIntent().getSerializableExtra("customer");
        if (existingCustomer != null) {
            // Pre-fill fields if editing an existing product
            editTextName.setText(existingCustomer.getName());
            editTextSurName.setText(existingCustomer.getSurname());
            editTextMiddleName.setText(existingCustomer.getSurname());
            editTextPhone.setText(existingCustomer.getPhone());
            editTextEmail.setText(existingCustomer.getEmail());

            if (existingCustomer.isDeleted()){
                editTextName.setEnabled(false);
                editTextSurName.setEnabled(false);
                editTextMiddleName.setEnabled(false);
                editTextPhone.setEnabled(false);
                editTextEmail.setEnabled(false);

                buttonSave.setVisibility(View.GONE);
                labelDeleted.setVisibility(View.VISIBLE);
            }
            else{
                // Show delete button only for existing item
                buttonDelete.setVisibility(View.VISIBLE);
            }
        }

        buttonSave.setOnClickListener(v -> saveCustomer());
        buttonDelete.setOnClickListener(v -> deleteCustomer());
    }

    private void deleteCustomer() {
        if (existingCustomer == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("customer", existingCustomer);
                    resultIntent.putExtra("delete", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void saveCustomer() {
        String name       = editTextName.getText().toString().trim();
        String surName    = editTextSurName.getText().toString().trim();
        String middleName = editTextMiddleName.getText().toString().trim();
        String phone      = editTextPhone.getText().toString().trim();
        String email      = editTextEmail.getText().toString().trim();

        Intent resultIntent = new Intent();

        if (existingCustomer != null) {
            // Update existing product
            existingCustomer.setName(name);
            existingCustomer.setSurname(surName);
            existingCustomer.setMiddleName(middleName);
            existingCustomer.setPhone(phone);
            existingCustomer.setEmail(email);
            viewModel.update(existingCustomer);
        } else {
            Customer customer = new Customer(name, surName, middleName, phone, email);
            resultIntent.putExtra("customer", customer);
        }

        resultIntent.putExtra("isNewCustomer", existingCustomer == null);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

