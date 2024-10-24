package com.liberty.turnovermanagement.orders.list;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.databinding.FragmentOrdersBinding;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.details.OrderDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private ActivityResultLauncher<Intent> detailsOrderLauncher;
    private OrderAdapter adapter;
    private OrderListViewModel viewModel;
    private FragmentOrdersBinding binding;
    private View emptyStateLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsOrderLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                });
    }

    private void openOrderDetailsActivity(Order order) {
        Intent intent = new Intent(requireContext(), OrderDetailsActivity.class);
        if (order != null) {
            intent.putExtra("orderId", order.getId());
        }
        detailsOrderLauncher.launch(intent);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(OrderListViewModel.class);

        binding = FragmentOrdersBinding.inflate(inflater, container, false);

        emptyStateLayout = inflater.inflate(R.layout.layout_empty_state, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        adapter = new OrderAdapter(this::openOrderDetailsActivity);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getOrders().observe(getViewLifecycleOwner(), this::updateOrderList);

        viewModel.canCreateOrder().observe(getViewLifecycleOwner(), canCreate -> {
            binding.fab.setOnClickListener(_view -> {
                if (canCreate) {
                    openOrderDetailsActivity(null);
                } else {
                    showImpossibleToCreateOrderNotification();
                }
            });
        });
    }

    private void updateOrderList(List<Order> orders) {
        if (orders.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            adapter.submitList(orders);
        }
    }

    private void showImpossibleToCreateOrderNotification() {
        Toast toast = Toast.makeText(getContext(), "Impossible to create order: no products or customers created", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}