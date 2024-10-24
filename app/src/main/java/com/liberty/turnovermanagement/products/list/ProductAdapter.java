package com.liberty.turnovermanagement.products.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.liberty.turnovermanagement.databinding.ProductListItemBinding;
import com.liberty.turnovermanagement.products.data.Product;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Creating ViewHolder objects to hold the views for each item.
 * Binding data to the views in the ViewHolder.
 * Handling item clicks (if needed).
 */
public class ProductAdapter extends ListAdapter<Product, ProductAdapter.ProductListItemViewHolder> {

    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(OnProductClickListener listener) {
        super(new ProductDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductListItemBinding binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductListItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListItemViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    /**
     * hold references to the views in your item layout
     */
    public static class ProductListItemViewHolder extends RecyclerView.ViewHolder {
        private final ProductListItemBinding binding;

        ProductListItemViewHolder(ProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product, ProductAdapter.OnProductClickListener listener) {
            binding.textViewName.setText(product.getName());
            binding.textViewAmount.setText("Amount: " + product.getAmount());
            binding.textViewPrice.setText("Price: $" + String.format("%.2f", product.getPrice()));
            itemView.setOnClickListener(v -> listener.onProductClick(product));
        }
    }

    static class ProductDiffCallback extends DiffUtil.ItemCallback<Product> {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    }
}
