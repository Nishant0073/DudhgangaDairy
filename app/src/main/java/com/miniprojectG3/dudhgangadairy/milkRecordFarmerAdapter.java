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

public class milkRecordFarmerAdapter extends ArrayAdapter<Record> {

    public Context mContext;

    public milkRecordFarmerAdapter(@NonNull Context context, ArrayList<Record> records) {
        super(context, 0, records);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;


        // Check if the existing view is being reused, otherwise inflate the view
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.farmer_milk_record_list, parent, false
            );
        }
        // Get the {@link Word} object located at this position in the list
        Record currentRecord = getItem(position);
//        TextView CustomerName = (TextView) listItemView.findViewById(R.id.LCustomerName);
//        CustomerName.setText(currentCustomer.name);
        TextView TFLRate = (TextView) listItemView.findViewById(R.id.FLRate);
        String sRate = Float.toString(currentRecord.rate);
        TFLRate.setText(sRate + "Rs");

        TextView TFLDate = (TextView) listItemView.findViewById(R.id.FLDate);
        TFLDate.setText(currentRecord.date);

        TextView TFLTime = (TextView) listItemView.findViewById(R.id.FLTime);
        String time = currentRecord.time.trim();
        TFLTime.setText(getStringIdentifier(getContext(),time));


        TextView TFLFat = (TextView) listItemView.findViewById(R.id.FLFat);
        String sFat = Float.toString(currentRecord.snf);
        TFLFat.setText(sFat);

        TextView TFLSnf = (TextView) listItemView.findViewById(R.id.FLSnf);
        String sSnf = Float.toString(currentRecord.snf);
        TFLSnf.setText(sSnf);

        TextView TFLWeight = (TextView) listItemView.findViewById(R.id.FLWeight);
        String sWeight = Float.toString(currentRecord.weight);
        TFLWeight.setText(sWeight);


        TextView TFLCost = (TextView) listItemView.findViewById(R.id.FLCost);
        String sCost = Float.toString(currentRecord.cost);
        TFLCost.setText(sCost + "Rs");

        TextView TFLMilkType = (TextView) listItemView.findViewById(R.id.FLMilkType);
        String milkType = currentRecord.milkType.trim();
        TFLMilkType.setText(getStringIdentifier(getContext(),milkType));


//        return super.getView(position, convertView, parent);
        return listItemView;
    }
    public static int getStringIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

}
