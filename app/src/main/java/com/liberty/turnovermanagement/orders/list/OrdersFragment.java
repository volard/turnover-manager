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

public class OrdersFragment extends Fragment {

    private ListView listView;
    private FloatingActionButton fab;
    private ActivityResultLauncher<Intent> detailsOrderLauncher;
    private ArrayAdapter<Order> adapter;
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
        View root = binding.getRoot();

        emptyStateLayout = inflater.inflate(R.layout.layout_empty_state, container, false);

        listView = root.findViewById(R.id.listView);

        // Set empty view for ListView
        listView.setEmptyView(emptyStateLayout);

        // Add the empty view to the parent layout
        ((ViewGroup) listView.getParent()).addView(emptyStateLayout);


        fab = root.findViewById(R.id.fab);

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
        listView.setAdapter(adapter);

        viewModel.getOrders().observe(getViewLifecycleOwner(), items -> {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();

            if (items.isEmpty()) {
                emptyStateLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                emptyStateLayout.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        viewModel.canCreateOrder().observe(getViewLifecycleOwner(), canCreate -> {
            fab.setOnClickListener(view -> {
                if (canCreate) {
                    openOrderDetailsActivity(null);
                } else {
                    showImpossibleToCreateOrderNotification();
                }
            });
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Order order = adapter.getItem(position);
            if (order != null) {
                openOrderDetailsActivity(order);
            }
        });

        return root;
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