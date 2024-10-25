package com.liberty.turnovermanagement.customers.details;


import android.view.LayoutInflater;
import android.view.View;

import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.ActivityDetailsCustomerBinding;
import com.liberty.turnovermanagement.base.details.BaseDetailsActivity;

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

        customer.setName(binding.editTextName.getText().toString().trim());
        customer.setSurname(binding.editTextSurName.getText().toString().trim());
        customer.setMiddleName(binding.editTextMiddleName.getText().toString().trim());
        customer.setPhone(binding.editTextPhone.getText().toString().trim());
        customer.setEmail(binding.editTextEmail.getText().toString().trim());

       return customer;
    }

}

