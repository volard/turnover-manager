package com.liberty.turnovermanagement.orders.create_update_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.liberty.turnovermanagement.products.data.Product;

import java.util.List;

public class ProductSpinnerAdapter extends ArrayAdapter<Product> {
    private final LayoutInflater layoutInflater;

    public ProductSpinnerAdapter(Context context, List<Product> products) {
        super(context, 0, products);
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
        Product product = getItem(position);

        if (product != null) {
            textView.setText(product.toString());
        }

        return convertView;
    }
}

