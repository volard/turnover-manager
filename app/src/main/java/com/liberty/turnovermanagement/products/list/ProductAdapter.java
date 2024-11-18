package com.liberty.turnovermanagement.products.list;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.base.list.BaseAdapter;
import com.liberty.turnovermanagement.databinding.ProductListItemBinding;
import com.liberty.turnovermanagement.products.data.Product;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Creating ViewHolder objects to hold the views for each item.
 * Binding data to the views in the ViewHolder.
 * Handling item clicks (if needed).
 */
public class ProductAdapter extends BaseAdapter<Product, ProductAdapter.ProductViewHolder> {

    private final Context context;

    public ProductAdapter(OnItemClickListener<Product> listener, Context context) {
        super(new ProductDiffCallback(), listener);
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductListItemBinding binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding, context);
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
        private final Context context;

        ProductViewHolder(ProductListItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        void bind(Product product, OnItemClickListener<Product> listener) {
            binding.textViewName.setText(product.getName());
            binding.textViewAmount.setText(context.getString(R.string.product_amount_format, product.getAmount()));


            String priceText = String.valueOf(product.getPrice());
            // русский просто добавляет пробелы, а так определяются запятые и точки, что привычнее и удобнее
            Locale locale = new Locale("en", "EN");
            try {
                // Format the price as currency
                NumberFormat currencyFormat = NumberFormat.getInstance(locale);
                Number price = currencyFormat.parse(priceText); // Parse the text
                String formattedPrice = currencyFormat.format(price); // Format as currency
                binding.textViewPrice.setText(context.getString(R.string.product_price_number, formattedPrice));
            } catch (ParseException e) {
                // Handle parsing errors
                Log.e("ProductDetails", "Error parsing price", e);
                binding.textViewPrice.setText(context.getString(R.string.product_price_format, product.getPrice()));
            }

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
