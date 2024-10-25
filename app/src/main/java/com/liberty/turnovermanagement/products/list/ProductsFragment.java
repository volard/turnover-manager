package com.liberty.turnovermanagement.products.list;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.details.ProductDetailsActivity;
import com.liberty.turnovermanagement.ui.BaseListFragment;

import java.util.List;
public class ProductsFragment extends BaseListFragment<Product, ProductListViewModel, ProductAdapter.ProductViewHolder> {
    @Override
    protected Class<ProductListViewModel> getViewModelClass() {
        return ProductListViewModel.class;
    }

    @Override
    protected void setupRecyclerView() {
        adapter = new ProductAdapter(this::openDetailsActivity);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void setupObservers() {
        viewModel.getItems().observe(getViewLifecycleOwner(), this::updateList);
    }

    @Override
    protected void setupFab() {
        binding.fab.setOnClickListener(v -> openDetailsActivity(null));
    }

    @Override
    protected void updateList(List<Product> products) {
        if (products.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            adapter.submitList(products);
        }
    }

    @Override
    protected void openDetailsActivity(Product product) {
        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
        if (product != null) {
            intent.putExtra("productId", product.getId());
        }
        startActivity(intent);
    }
}
