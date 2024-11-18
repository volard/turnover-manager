package com.liberty.turnovermanagement.base.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.liberty.turnovermanagement.base.Constants;
import com.liberty.turnovermanagement.base.Identifiable;
import com.liberty.turnovermanagement.databinding.FragmentListBinding;

import java.util.List;

public abstract class BaseListFragment<T extends Identifiable, VM extends BaseListViewModel<T>, VH extends RecyclerView.ViewHolder> extends Fragment {
    protected VM viewModel;
    protected BaseAdapter<T, VH> adapter;
    protected FragmentListBinding binding;
    protected ActivityResultLauncher<Intent> detailsLauncher;

    protected abstract Class<VM> getViewModelClass();

    protected abstract Class<?> getDetailsActivityClass();

    protected abstract Class<?> getCreateActivityClass();

    protected abstract void setupRecyclerView();

    protected abstract void setupObservers();

    private final boolean isSearchInitialized = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
        detailsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Handle result if needed
                }
        );
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSearchView();
        setupRecyclerView();
        setupObservers();

        binding.fab.setOnClickListener(v -> openCreateActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void showEmptyState() {
        binding.emptyStateLayout.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }

    protected void hideEmptyState() {
        binding.emptyStateLayout.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    protected void updateList(List<T> items) {
        if (items.isEmpty()) {
            showEmptyState();
            binding.searchView.setVisibility(View.GONE);
        } else {
            binding.searchView.setVisibility(View.VISIBLE);
            hideEmptyState();
            adapter.setItems(items);
            adapter.getFilter().filter(""); // trigger search view update to show all items
        }
    }

    private void sendItemToActivity(T item, Class<?> activityClass) {
        Intent intent = new Intent(requireContext(), activityClass);
        if (item != null) {
            intent.putExtra(Constants.ITEM_ID, item.getId());
        }
        detailsLauncher.launch(intent);
    }

    protected void openCreateActivity() {
        sendItemToActivity(null, getCreateActivityClass());
    }

    protected void openDetailsActivity(T item) {
        sendItemToActivity(item, getDetailsActivityClass());
    }

}

