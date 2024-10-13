package com.liberty.turnovermanagement.products;

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

public class ProductsFragment extends Fragment {

    private ListView listView;
    private FloatingActionButton fab;
    private ActivityResultLauncher<Intent> addEditProductLauncher;
    private ArrayAdapter<Product> adapter;
    private ProductsViewModel viewModel;
    private FragmentProductsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addEditProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Product product = (Product) data.getSerializableExtra("product");
                            boolean isNewProduct = data.getBooleanExtra("isNewProduct", true);
                            if (product != null) {
                                if (isNewProduct) {
                                    viewModel.addNewProduct(product);
                                } else {
                                    viewModel.updateProduct(product);
                                }
                            }
                        }
                    }
                });
    }

    private void openAddEditProductActivity(Product product) {
        Intent intent = new Intent(requireContext(), AddProductActivity.class);
        if (product != null) {
            intent.putExtra("product", product);
        }
        addEditProductLauncher.launch(intent);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.listView);
        fab = root.findViewById(R.id.fab);

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
        listView.setAdapter(adapter);

        viewModel.getProducts().observe(getViewLifecycleOwner(), items -> {
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