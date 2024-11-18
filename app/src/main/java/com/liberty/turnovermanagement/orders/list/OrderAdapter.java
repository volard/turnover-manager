package com.liberty.turnovermanagement.orders.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.base.list.BaseAdapter;
import com.liberty.turnovermanagement.databinding.OrderListItemBinding;
import com.liberty.turnovermanagement.orders.data.Order;

import java.time.format.DateTimeFormatter;

public class OrderAdapter extends BaseAdapter<Order, OrderAdapter.OrderViewHolder> {
    private final Context context;

    public OrderAdapter(OnItemClickListener<Order> listener, Context context) {
        super(new OrderDiffCallback(), listener);
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderListItemBinding binding = OrderListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    @Override
    protected boolean isItemFiltered(Order order, String filterPattern) {
        return (order.getCity() + order.getStreet() + order.getHome() + order.getCreatedAt())
                .toLowerCase().contains(filterPattern);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final OrderListItemBinding binding;
        private final Context context;

        OrderViewHolder(OrderListItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        void bind(final Order order, final OnItemClickListener<Order> listener) {
            binding.textViewOrderId.setText(context.getString(R.string.order_item__headline, order.getId()));
            binding.textViewDateTime.setText(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            binding.textViewAmount.setText(context.getString(R.string.product_amount_format, order.getAmount()));
            binding.textViewAddress.setText(String.format("%s, %s %s", order.getCity(), order.getStreet(), order.getHome()));

            itemView.setOnClickListener(v -> listener.onItemClick(order));
        }
    }

    static class OrderDiffCallback extends DiffUtil.ItemCallback<Order> {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.equals(newItem);
        }
    }
}
