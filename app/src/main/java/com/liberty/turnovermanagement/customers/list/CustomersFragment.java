package com.liberty.turnovermanagement.customers.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.details.CustomerDetailsActivity;
import com.liberty.turnovermanagement.databinding.FragmentProductsBinding;

import java.util.ArrayList;
import java.util.List;

public class CustomersFragment extends Fragment {
    private ActivityResultLauncher<Intent> customerDetailsActivity;
   private CustomerAdapter adapter;
    private CustomerListViewModel viewModel;
    private FragmentProductsBinding binding;
    private View emptyStateLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CustomerListViewModel.class);

        customerDetailsActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Customer list will be automatically updated through LiveData
            }
        );
    }

    private void openCustomerDetailsActivity(Customer customer) {
        Intent intent = new Intent(requireContext(), CustomerDetailsActivity.class);
        if (customer != null) {
            intent.putExtra("customerId", customer.getId());
        }
        customerDetailsActivity.launch(intent);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProductsBinding.inflate(inflater, container, false);

        emptyStateLayout = inflater.inflate(R.layout.layout_empty_state, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        adapter = new CustomerAdapter(this::openCustomerDetailsActivity);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getCustomers().observe(getViewLifecycleOwner(), this::updateCustomerList);

        binding.fab.setOnClickListener(v -> openCustomerDetailsActivity(null));

    }
    private void updateCustomerList(List<Customer> customers) {
        if (customers.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            adapter.submitList(customers);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}