package com.liberty.turnovermanagement.products.list;

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
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.details.ProductDetailsActivity;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    private ListView listView;
    private FloatingActionButton fab;
    private ActivityResultLauncher<Intent> addEditProductLauncher;
    private ArrayAdapter<Product> adapter;
    private FragmentProductsBinding binding;
    private View emptyStateLayout;
    private ProductListViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        addEditProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Product list will be automatically updated through LiveData
            }
        );
    }

    private void openAddEditProductActivity(Product product) {
        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
        if (product != null) {
            intent.putExtra("productId", product.getId());
        }
        addEditProductLauncher.launch(intent);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.listView);
        fab = root.findViewById(R.id.fab);

        emptyStateLayout = inflater.inflate(R.layout.layout_empty_state, container, false);

        // Set empty view for ListView
        listView.setEmptyView(emptyStateLayout);

        // Add the empty view to the parent layout
        ((ViewGroup) listView.getParent()).addView(emptyStateLayout);

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
        listView.setAdapter(adapter);

        viewModel.getProducts().observe(this, items -> {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Product product = adapter.getItem(position);
            if (product != null) {
                openAddEditProductActivity(product);
            }
        });

        fab.setOnClickListener(v -> openAddEditProductActivity(null));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}