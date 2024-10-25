package com.liberty.turnovermanagement.orders.list;


import android.content.Intent;

import android.view.View;

import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;


import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.details.OrderDetailsActivity;
import com.liberty.turnovermanagement.ui.BaseListFragment;

import java.util.List;
public class OrdersFragment extends BaseListFragment<Order, OrderListViewModel, OrderAdapter.OrderViewHolder> {
    @Override
    protected Class<OrderListViewModel> getViewModelClass() {
        return OrderListViewModel.class;
    }

    @Override
    protected void setupRecyclerView() {
        adapter = new OrderAdapter(this::openDetailsActivity);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void setupObservers() {
        viewModel.getItems().observe(getViewLifecycleOwner(), this::updateList);
        viewModel.canCreateOrder().observe(getViewLifecycleOwner(), this::updateFabVisibility);
    }

    @Override
    protected void setupFab() {
        binding.fab.setOnClickListener(v -> {
            if (viewModel.canCreateOrder().getValue() == Boolean.TRUE) {
                openDetailsActivity(null);
            } else {
                showImpossibleToCreateOrderNotification();
            }
        });
    }



    @Override
    protected void updateList(List<Order> orders) {
        if (orders.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            adapter.submitList(orders);
        }
    }

    @Override
    protected void openDetailsActivity(Order order) {
        Intent intent = new Intent(requireContext(), OrderDetailsActivity.class);
        if (order != null) {
            intent.putExtra("orderId", order.getId());
        }
        startActivity(intent);
    }

    private void updateFabVisibility(Boolean canCreate) {
        binding.fab.setVisibility(canCreate ? View.VISIBLE : View.GONE);
    }

    private void showImpossibleToCreateOrderNotification() {
        Toast.makeText(getContext(), "Impossible to create order: no products or customers created", Toast.LENGTH_SHORT).show();
    }
}
