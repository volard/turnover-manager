package com.liberty.turnovermanagement.orders.details;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.liberty.turnovermanagement.customers.Customer;
import com.liberty.turnovermanagement.products.Product;

import java.util.List;

public class CustomerSpinnerAdapter extends ArrayAdapter<Customer> {
    private final LayoutInflater layoutInflater;

    public CustomerSpinnerAdapter(Context context, List<Customer> customers) {
        super(context, 0, customers);
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    /**
     * Gets a View that displays in the drop down popup the data at the specified
     * position in the data set.
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Customer customer = getItem(position);

        if (customer != null) {
            // Customize this to display the information you want
            String displayText = String.format(
                    "%d - %s %s %s",
                    customer.getId(),
                    customer.getSurname(),
                    customer.getName(),
                    customer.getMiddleName()
            );
            textView.setText(displayText);
        }

        return convertView;
    }
}

