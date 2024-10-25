package com.liberty.turnovermanagement.customers.details;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.ActivityDetailsCustomerBinding;

public class CustomerDetailsActivity extends AppCompatActivity {

    private CustomerDetailsViewModel viewModel;
    private long customerId = -1;
    private ActivityDetailsCustomerBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CustomerDetailsViewModel.class);


        customerId = getIntent().getLongExtra("customerId", -1);

        if (customerId != -1) {
            viewModel.loadItem(customerId);
        }

        viewModel.getSelectedItem().observe(this, this::updateUI);


        binding.buttonSave.setOnClickListener(v -> saveCustomer());
        binding.buttonDelete.setOnClickListener(v -> deleteCustomer());
    }

    private void updateUI(Customer customer) {
        if (customer == null) {
            return;
        }
        binding.editTextName.setText(customer.getName());
        binding.editTextSurName.setText(customer.getSurname());
        binding.editTextMiddleName.setText(customer.getMiddleName());
        binding.editTextPhone.setText(customer.getPhone());
        binding.editTextEmail.setText(customer.getEmail());

        if (customer.isDeleted()) {
            binding.editTextName.setEnabled(false);
            binding.editTextSurName.setEnabled(false);
            binding.editTextMiddleName.setEnabled(false);
            binding.editTextPhone.setEnabled(false);
            binding.editTextEmail.setEnabled(false);
            binding.buttonSave.setVisibility(View.GONE);
            binding.labelDeleted.setVisibility(View.VISIBLE);
        } else {
            binding.buttonDelete.setVisibility(View.VISIBLE);
        }
    }

    private void setupVersionHistory() {
        viewModel.getItemHistory(customerId).observe(this, history -> {
            // Display the version history, e.g., in a RecyclerView
            // You'll need to create a new adapter and layout for this
        });
    }

    private void deleteCustomer() {
        Customer customer = viewModel.getSelectedItem().getValue();
        if (customer == null) {
            return;
        }
        viewModel.softDelete(customer);
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void saveCustomer() {
        Customer customer = viewModel.getSelectedItem().getValue();
        if (customer == null) {
            customer = new Customer();
        }

        customer.setName(binding.editTextName.getText().toString().trim());
        customer.setSurname(binding.editTextSurName.getText().toString().trim());
        customer.setMiddleName(binding.editTextMiddleName.getText().toString().trim());
        customer.setPhone(binding.editTextPhone.getText().toString().trim());
        customer.setEmail(binding.editTextEmail.getText().toString().trim());

        if (customerId == -1) {
            viewModel.addNewItem(customer);
        } else {
            viewModel.updateItem(customer);
        }

        setResult(Activity.RESULT_OK);
        finish();
    }
}

