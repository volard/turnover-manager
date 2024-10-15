package com.liberty.turnovermanagement.customers;

import android.app.Activity;
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
import com.liberty.turnovermanagement.databinding.FragmentProductsBinding;

import java.util.ArrayList;

public class CustomersFragment extends Fragment {
    private ListView listView;
    private FloatingActionButton fab;
    private ActivityResultLauncher<Intent> customerDetailsActivity;
    private ArrayAdapter<Customer> adapter;
    private CustomersViewModel viewModel;
    private FragmentProductsBinding binding;
    private View emptyStateLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerDetailsActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Customer customer = (Customer) data.getSerializableExtra("customer");
                        boolean isNewProduct = data.getBooleanExtra("isNewCustomer", true);
                        boolean isDelete = data.getBooleanExtra("delete", false);
                        if (customer != null) {
                            if (isDelete) {
                                viewModel.softDelete(customer);
                            } else if (isNewProduct) {
                                viewModel.addNew(customer);
                            } else {
                                viewModel.update(customer);
                            }
                        }
                    }
                }
            }
        );
    }

    private void openCustomerDetailsActivity(Customer customer) {
        Intent intent = new Intent(requireContext(), CustomerDetailsActivity.class);
        if (customer != null) {
            intent.putExtra("customer", customer);
        }
        customerDetailsActivity.launch(intent);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CustomersViewModel.class);

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