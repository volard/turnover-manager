package com.liberty.turnovermanagement.products.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.liberty.turnovermanagement.base.list.BaseAdapter;
import com.liberty.turnovermanagement.databinding.ProductListItemBinding;
import com.liberty.turnovermanagement.products.data.Product;

/**
 * Creating ViewHolder objects to hold the views for each item.
 * Binding data to the views in the ViewHolder.
 * Handling item clicks (if needed).
 */
public class ProductAdapter extends BaseAdapter<Product, ProductAdapter.ProductViewHolder> {

    public ProductAdapter(OnItemClickListener<Product> listener) {
        super(new ProductDiffCallback(), listener);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductListItemBinding binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    @Override
    protected boolean isItemFiltered(Product product, String filterPattern) {
        return product.getName().toLowerCase().contains(filterPattern);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ProductListItemBinding binding;

        ProductViewHolder(ProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product, OnItemClickListener<Product> listener) {
            binding.textViewName.setText(product.getName());
            binding.textViewAmount.setText("Amount: " + product.getAmount());
            binding.textViewPrice.setText("Price: $ " + String.format("%.2f", product.getPrice()));
            itemView.setOnClickListener(v -> listener.onItemClick(product));
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
