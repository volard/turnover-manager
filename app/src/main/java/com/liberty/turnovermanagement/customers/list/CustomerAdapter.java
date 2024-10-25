package com.liberty.turnovermanagement.customers.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.databinding.CustomerListItemBinding;
import com.liberty.turnovermanagement.ui.BaseAdapter;

public class CustomerAdapter extends BaseAdapter<Customer, CustomerAdapter.CustomerViewHolder> {

    public CustomerAdapter(OnItemClickListener<Customer> listener) {
        super(new CustomerDiffCallback(), listener);
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

    @Override
    protected boolean isItemFiltered(Customer customer, String filterPattern) {
        return (customer.getName() + customer.getMiddleName() + customer.getSurname()
                + customer.getPhone() + customer.getEmail()).toLowerCase().contains(filterPattern);
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        private final CustomerListItemBinding binding;

        CustomerViewHolder(CustomerListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Customer customer, final OnItemClickListener<Customer> listener) {
            String fullName = customer.getSurname() + " " + customer.getName() + " " + customer.getMiddleName();
            binding.textViewName.setText(fullName);
            binding.textViewPhone.setText(customer.getPhone());
            binding.textViewEmail.setText(customer.getEmail());

            itemView.setOnClickListener(v -> listener.onItemClick(customer));
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
