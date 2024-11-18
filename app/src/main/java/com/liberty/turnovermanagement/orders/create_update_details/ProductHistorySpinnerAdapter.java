package com.liberty.turnovermanagement.orders.create_update_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.liberty.turnovermanagement.base.Constants;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductHistory;

import java.util.List;

public class ProductHistorySpinnerAdapter extends ArrayAdapter<ProductHistory> {
    private final LayoutInflater layoutInflater;
    private final ProductHistory actualVersion;


    public ProductHistorySpinnerAdapter(Context context, List<ProductHistory> products, Product actualProduct) {
        super(context, 0, products);

        actualVersion = actualProduct.toHistory();
        products.add(0, actualVersion);
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
        ProductHistory currentProductHistory = getItem(position);

        if (currentProductHistory != null) {

            if (currentProductHistory.getVersion() == actualVersion.getVersion()) {
                textView.setText("Actual version");
            } else {
                // Customize this to display the information you want
                textView.setText(currentProductHistory.toString());
            }

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

