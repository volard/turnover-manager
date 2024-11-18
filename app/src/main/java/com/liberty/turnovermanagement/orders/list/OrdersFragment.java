package com.liberty.turnovermanagement.orders.list;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.liberty.turnovermanagement.base.list.BaseListFragment;
import com.liberty.turnovermanagement.orders.create_update_details.OrderEditActivity;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.details.OrderDetailsActivity;

public class OrdersFragment extends BaseListFragment<Order, OrderListViewModel, OrderAdapter.OrderViewHolder> {
    @Override
    protected Class<OrderListViewModel> getViewModelClass() {
        return OrderListViewModel.class;
    }

    @Override
    protected Class<?> getDetailsActivityClass() {
        return OrderDetailsActivity.class;
    }

    @Override
    protected Class<?> getCreateActivityClass() {
        return OrderEditActivity.class;
    }

    @Override
    protected void setupRecyclerView() {
        adapter = new OrderAdapter(this::openDetailsActivity, getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void setupObservers() {
        viewModel.getItems().observe(getViewLifecycleOwner(), this::updateList);
        viewModel.canCreateOrder().observe(getViewLifecycleOwner(), this::updateFabVisibility);
    }

    private void updateFabVisibility(Boolean canCreate) {
        binding.fab.setVisibility(canCreate ? View.VISIBLE : View.GONE);
    }
}
