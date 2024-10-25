package com.liberty.turnovermanagement.orders.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.liberty.turnovermanagement.databinding.OrderListItemBinding;
import com.liberty.turnovermanagement.orders.data.Order;

import java.time.format.DateTimeFormatter;

public class OrderAdapter extends ListAdapter<Order, OrderAdapter.OrderViewHolder> {

    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(OnOrderClickListener listener) {
        super(new OrderDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderListItemBinding binding = OrderListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final OrderListItemBinding binding;

        OrderViewHolder(OrderListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Order order, final OnOrderClickListener listener) {
            binding.textViewOrderId.setText(String.format("Order #%d", order.getId()));
            binding.textViewDateTime.setText(order.getDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            binding.textViewAmount.setText(String.format("Amount: %d", order.getAmount()));
            binding.textViewAddress.setText(String.format("%s, %s %s", order.getCity(), order.getStreet(), order.getHome()));

            itemView.setOnClickListener(v -> listener.onOrderClick(order));
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
