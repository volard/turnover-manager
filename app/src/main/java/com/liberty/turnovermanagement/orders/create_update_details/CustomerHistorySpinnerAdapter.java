package com.liberty.turnovermanagement.orders.create_update_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.liberty.turnovermanagement.base.Constants;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;

import java.util.List;

public class CustomerHistorySpinnerAdapter extends ArrayAdapter<CustomerHistory> {
    private final LayoutInflater layoutInflater;


    public CustomerHistorySpinnerAdapter(Context context, List<CustomerHistory> products) {
        super(context, 0, products);
        CustomerHistory currentItemShadow = new CustomerHistory();
        currentItemShadow.setId(Constants.UNINITIALIZED_INDICATOR);
        products.add(0, currentItemShadow);
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file.
     */
    @NonNull
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
        CustomerHistory currentCustomerHistory = getItem(position);

        if (currentCustomerHistory != null) {

            if (currentCustomerHistory.getId() == Constants.UNINITIALIZED_INDICATOR) {
                textView.setText("Actual version");
            } else {
                // Customize this to display the information you want
                String displayText = currentCustomerHistory.getId() + " - " + currentCustomerHistory.getSurname() + " " + currentCustomerHistory.getName() + " " + currentCustomerHistory.getMiddleName();
                textView.setText(displayText);
            }

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

