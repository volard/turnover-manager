package com.liberty.turnovermanagement.customers.list;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.details.CustomerDetailsActivity;
import com.liberty.turnovermanagement.ui.BaseListFragment;

import java.util.List;


public class CustomersFragment extends BaseListFragment<Customer, CustomerListViewModel, CustomerAdapter.CustomerViewHolder> {
    @Override
    protected Class<CustomerListViewModel> getViewModelClass() {
        return CustomerListViewModel.class;
    }

    @Override
    protected void setupRecyclerView() {
        adapter = new CustomerAdapter(this::openDetailsActivity);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void setupObservers() {
        viewModel.getCustomers().observe(getViewLifecycleOwner(), this::updateList);
    }

    @Override
    protected void setupFab() {
        binding.fab.setOnClickListener(v -> openDetailsActivity(null));
    }

    @Override
    protected void openDetailsActivity(Customer customer) {
        Intent intent = new Intent(requireContext(), CustomerDetailsActivity.class);
        if (customer != null) {
            intent.putExtra("customerId", customer.getId());
        }
        detailsLauncher.launch(intent);
    }

    @Override
    protected void updateList(List<Customer> customers) {
        if (customers.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            adapter.submitList(customers);
        }
    }
}
