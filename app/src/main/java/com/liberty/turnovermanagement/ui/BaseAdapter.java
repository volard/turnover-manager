package com.liberty.turnovermanagement.ui;

import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder>
        extends ListAdapter<T, VH> implements Filterable {

    protected List<T> itemsFull;
    protected List<T> itemsFiltered;
    protected final OnItemClickListener<T> listener;

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    protected BaseAdapter(DiffUtil.ItemCallback<T> diffCallback, OnItemClickListener<T> listener) {
        super(diffCallback);
        this.listener = listener;
        this.itemsFull = new ArrayList<>();
        this.itemsFiltered = new ArrayList<>();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterPattern = constraint.toString().toLowerCase().trim();
                List<T> filteredList = new ArrayList<>();

                if (filterPattern.isEmpty()) {
                    filteredList.addAll(itemsFull);
                } else {
                    for (T item : itemsFull) {
                        if (isItemFiltered(item, filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsFiltered = (List<T>) results.values;
                submitList(itemsFiltered);
            }
        };
    }

    public void setItems(List<T> items) {
        itemsFull = new ArrayList<>(items);
        itemsFiltered = new ArrayList<>(items);
        submitList(itemsFiltered);
    }

    protected abstract boolean isItemFiltered(T item, String filterPattern);
}

