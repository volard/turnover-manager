package com.liberty.turnovermanagement.products.list;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.liberty.turnovermanagement.base.list.BaseListFragment;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.details.ProductDetailsActivity;

public class ProductsFragment extends BaseListFragment<Product, ProductListViewModel, ProductAdapter.ProductViewHolder> {
    @Override
    protected Class<ProductListViewModel> getViewModelClass() {
        return ProductListViewModel.class;
    }

    @Override
    protected Class<ProductDetailsActivity> getDetailsActivityClass() {
        return ProductDetailsActivity.class;
    }

    @Override
    protected Class<?> getCreateActivityClass() {
        return ProductDetailsActivity.class;
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
}
