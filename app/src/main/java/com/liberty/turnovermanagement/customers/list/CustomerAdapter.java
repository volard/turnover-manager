package com.liberty.turnovermanagement.customers.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.CustomerListItemBinding;

public class CustomerAdapter extends ListAdapter<Customer, CustomerAdapter.CustomerViewHolder> {

    private final OnCustomerClickListener listener;

    public interface OnCustomerClickListener {
        void onCustomerClick(Customer customer);
    }

    public CustomerAdapter(OnCustomerClickListener listener) {
        super(new CustomerDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomerListItemBinding binding = CustomerListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CustomerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        private final CustomerListItemBinding binding;

        CustomerViewHolder(CustomerListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Customer customer, final OnCustomerClickListener listener) {
            String fullName = customer.getSurname() + " " + customer.getName() + " " + customer.getMiddleName();
            binding.textViewName.setText(fullName);
            binding.textViewPhone.setText(customer.getPhone());
            binding.textViewEmail.setText(customer.getEmail());

            itemView.setOnClickListener(v -> listener.onCustomerClick(customer));
        }
    }

    static class CustomerDiffCallback extends DiffUtil.ItemCallback<Customer> {
        @Override
        public boolean areItemsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.equals(newItem);
        }
    }
}
