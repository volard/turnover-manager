package com.liberty.turnovermanagement.customers.details;

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
import com.liberty.turnovermanagement.customers.data.Customer;

public class CustomerDetailsActivity extends AppCompatActivity {

    private EditText editTextSurName, editTextName, editTextMiddleName, editTextPhone, editTextEmail;
    private Button buttonSave, buttonDelete;
    private TextView labelDeleted;
    private CustomerDetailsViewModel viewModel;
    private int customerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_customer);

        viewModel = new ViewModelProvider(this).get(CustomerDetailsViewModel.class);


        editTextName       = findViewById(R.id.editTextName);
        editTextSurName    = findViewById(R.id.editTextSurName);
        editTextMiddleName = findViewById(R.id.editTextMiddleName);
        editTextPhone      = findViewById(R.id.editTextPhone);
        editTextEmail      = findViewById(R.id.editTextEmail);
        buttonSave         = findViewById(R.id.buttonSave);
        buttonDelete       = findViewById(R.id.buttonDelete);
        labelDeleted       = findViewById(R.id.labelDeleted);

        customerId = getIntent().getIntExtra("customerId", -1);

        if (customerId != -1) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                Customer customer = AppDatabase.getDatabase(this).customerDao().getCustomerById(customerId);
                runOnUiThread(() -> viewModel.setSelectedCustomer(customer));
            });
        }

        viewModel.getSelectedCustomer().observe(this, this::updateUI);


        buttonSave.setOnClickListener(v -> saveCustomer());
        buttonDelete.setOnClickListener(v -> deleteCustomer());
    }

    private void updateUI(Customer customer) {
        if (customer != null) {
            editTextName.setText(customer.getName());
            editTextSurName.setText(customer.getSurname());
            editTextMiddleName.setText(customer.getMiddleName());
            editTextPhone.setText(customer.getPhone());
            editTextEmail.setText(customer.getEmail());

            if (customer.isDeleted()) {
                editTextName.setEnabled(false);
                editTextSurName.setEnabled(false);
                editTextMiddleName.setEnabled(false);
                editTextPhone.setEnabled(false);
                editTextEmail.setEnabled(false);
                buttonSave.setVisibility(View.GONE);
                labelDeleted.setVisibility(View.VISIBLE);
            } else {
                buttonDelete.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupVersionHistory() {
        viewModel.getCustomerHistory(customerId).observe(this, history -> {
            // Display the version history, e.g., in a RecyclerView
            // You'll need to create a new adapter and layout for this
        });
    }

    private void deleteCustomer() {
        Customer customer = viewModel.getSelectedCustomer().getValue();
        if (customer != null) {
            viewModel.softDelete(customer);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void saveCustomer() {
        Customer customer = viewModel.getSelectedCustomer().getValue();
        if (customer == null) {
            customer = new Customer();
        }

        customer.setName(editTextName.getText().toString().trim());
        customer.setSurname(editTextSurName.getText().toString().trim());
        customer.setMiddleName(editTextMiddleName.getText().toString().trim());
        customer.setPhone(editTextPhone.getText().toString().trim());
        customer.setEmail(editTextEmail.getText().toString().trim());

        if (customerId == -1) {
            viewModel.addNewCustomer(customer);
        } else {
            viewModel.updateCustomer(customer);
        }

        setResult(Activity.RESULT_OK);
        finish();
    }
}

