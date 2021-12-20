package com.miniprojectG3.dudhgangadairy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomerAdapter extends ArrayAdapter<Customer> {

    public CustomerAdapter(@NonNull Context context, ArrayList<Customer> customers) {
        super(context, 0, customers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;


        // Check if the existing view is being reused, otherwise inflate the view
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }
        // Get the {@link Word} object located at this position in the list
        Customer currentCustomer = getItem(position);

        TextView CustomerName = (TextView) listItemView.findViewById(R.id.LCustomerName);
        CustomerName.setText(currentCustomer.name);

        TextView CustomerId = (TextView) listItemView.findViewById(R.id.LCustomerID);
        CustomerId.setText(String.valueOf(currentCustomer.uniqueId));

        TextView CustomerPhoneNumber = (TextView) listItemView.findViewById(R.id.LCustomerPhoneNumber);
        CustomerPhoneNumber.setText(currentCustomer.phoneNumber);

//        return super.getView(position, convertView, parent);
        return listItemView;
    }
}
