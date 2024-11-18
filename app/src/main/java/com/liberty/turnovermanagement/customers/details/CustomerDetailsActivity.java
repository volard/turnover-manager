package com.liberty.turnovermanagement.customers.details;


import android.view.LayoutInflater;
import android.view.View;

import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.ActivityDetailsCustomerBinding;

public class CustomerDetailsActivity extends BaseDetailsActivity<Customer, CustomerDetailsViewModel, ActivityDetailsCustomerBinding> {

    @Override
    protected Class<CustomerDetailsViewModel> getViewModelClass() {
        return CustomerDetailsViewModel.class;
    }

    @Override
    protected ActivityDetailsCustomerBinding inflateBinding(LayoutInflater inflater) {
        return ActivityDetailsCustomerBinding.inflate(inflater);
    }

    @Override
    protected void setupButtons() {
        binding.buttonSave.setOnClickListener(v -> saveOrUpdateItem());
        binding.buttonDelete.setOnClickListener(v -> deleteItem());
    }

    @Override
    protected void updateUI(Customer customer) {
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

    @Override
    protected Customer getItemToSaveOrUpdate() {
        Customer customer = viewModel.getSelectedItem().getValue();
        if (customer == null) {
            customer = new Customer();
        }

        String surname = binding.editTextSurName.getText().toString().trim();
        String name = binding.editTextName.getText().toString().trim();
        String middleName = binding.editTextMiddleName.getText().toString().trim();
        String phone = binding.editTextPhone.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();

        // Validation checks
        if (surname.isEmpty()) {
            binding.editTextSurName.setError("Surname cannot be empty");
            return null;
        }

        if (name.isEmpty()) {
            binding.editTextName.setError("Name cannot be empty");
            return null;
        }

        if (phone.isEmpty()) {
            binding.editTextPhone.setError("Phone number cannot be empty");
            return null;
        } else if (!isValidPhoneNumber(phone)) {
            binding.editTextPhone.setError("Invalid phone number format");
            return null;
        }

        if (!email.isEmpty() && !isValidEmail(email)) {
            binding.editTextEmail.setError("Invalid email format");
            return null;
        }

        // If all validations pass, set the values
        customer.setSurname(surname);
        customer.setName(name);
        customer.setMiddleName(middleName);
        customer.setPhone(phone);
        customer.setEmail(email);

        return customer;
    }

    // Helper method to validate phone number format
    private boolean isValidPhoneNumber(String phone) {
        // This is a simple regex for phone number validation
        // You might want to adjust it based on your specific requirements
        String phoneRegex = "^\\+?[0-9]{10,14}$";
        return phone.matches(phoneRegex);
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

}

