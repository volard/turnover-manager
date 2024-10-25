package com.liberty.turnovermanagement.products.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.databinding.FragmentProductsBinding;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.details.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private ProductAdapter adapter;
    private ActivityResultLauncher<Intent> addEditProductLauncher;
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProductsBinding.inflate(inflater, container, false);

        emptyStateLayout = inflater.inflate(R.layout.layout_empty_state, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Add the empty view to the parent layout
        ((ViewGroup) binding.recyclerView.getParent()).addView(emptyStateLayout);

        // Handle product click
        adapter = new ProductAdapter(this::openAddEditProductActivity);
        binding.recyclerView.setAdapter(adapter);

        setupSearchView();


        viewModel.getProducts().observe(getViewLifecycleOwner(), this::updateProductList);

        binding.fab.setOnClickListener(v -> openAddEditProductActivity(null));
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        binding.searchView.setOnCloseListener(() -> {
            adapter.getFilter().filter("");
            return false;
        });
    }


    private void updateProductList(List<Product> products) {
        if (products.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            adapter.setProducts(products);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}