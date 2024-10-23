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
import com.liberty.turnovermanagement.customers.details.CustomerDetailsActivity;
import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.FragmentProductsBinding;

import java.util.ArrayList;

public class CustomersFragment extends Fragment {
    private ListView listView;
    private FloatingActionButton fab;
    private ActivityResultLauncher<Intent> customerDetailsActivity;
    private ArrayAdapter<Customer> adapter;
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.listView);

        emptyStateLayout = inflater.inflate(R.layout.layout_empty_state, container, false);

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

        viewModel.getCustomers().observe(getViewLifecycleOwner(), items -> {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Customer customer = adapter.getItem(position);
            if (customer != null) {
                openCustomerDetailsActivity(customer);
            }
        });

        fab.setOnClickListener(v -> openCustomerDetailsActivity(null));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}