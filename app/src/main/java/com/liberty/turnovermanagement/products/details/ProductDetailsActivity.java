package com.liberty.turnovermanagement.products.details;


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
    protected Class<ProductDetailViewModel> getViewModelClass() {
        return ProductDetailViewModel.class;
    }

    @Override
    protected ActivityDetailsProductBinding inflateBinding(LayoutInflater inflater) {
       return ActivityDetailsProductBinding.inflate(inflater);
    }

    @Override
    protected void updateUI(Product product) {
        if (product != null) {
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
    }

    @Override
    protected Product getItemToSaveOrUpdate() {
        Product product = viewModel.getSelectedItem().getValue();
        if (product == null) {
            product = new Product();
        }

        product.setName(binding.editTextName.getText().toString());
        product.setAmount(Integer.parseInt(binding.editTextAmount.getText().toString()));
        product.setPrice(Double.parseDouble(binding.editTextPrice.getText().toString()));

        return product;
    }
}
