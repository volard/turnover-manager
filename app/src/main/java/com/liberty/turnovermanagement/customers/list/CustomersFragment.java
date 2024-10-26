package com.liberty.turnovermanagement.customers.list;


import androidx.recyclerview.widget.LinearLayoutManager;

import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.base.list.BaseListFragment;
import com.liberty.turnovermanagement.customers.details.CustomerDetailsActivity;


public class CustomersFragment extends BaseListFragment<Customer, CustomerListViewModel, CustomerAdapter.CustomerViewHolder> {
    @Override
    protected Class<CustomerListViewModel> getViewModelClass() {
        return CustomerListViewModel.class;
    }

    @Override
    protected Class<?> getDetailsActivityClass() {
        return CustomerDetailsActivity.class;
    }

    @Override
    protected Class<?> getCreateActivityClass() {
        return CustomerDetailsActivity.class;
    }

    @Override
    protected void setupRecyclerView() {
        adapter = new CustomerAdapter(this::openDetailsActivity);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void setupObservers() {
        viewModel.getItems().observe(getViewLifecycleOwner(), this::updateList);
    }
}
