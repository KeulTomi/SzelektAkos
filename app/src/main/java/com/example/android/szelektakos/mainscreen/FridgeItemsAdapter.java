package com.example.android.szelektakos.mainscreen;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.szelektakos.R;
import com.example.android.szelektakos.shop.Items;

import java.util.ArrayList;

/**
 * Created by Tomi on 2017. 03. 28..
 */

public class FridgeItemsAdapter extends ArrayAdapter<Items> {

    Activity activity;

    FridgeItemsAdapter(Activity context, ArrayList<Items> items) {
        super(context, 0, items);
        activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        final View itemView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.fridge_list_item, parent, false);

        }

        Items currentItem = getItem(position);


        TextView foodLifeValue = (TextView) listItemView.findViewById(R.id.fridge_item_value);
        foodLifeValue.setText(String.valueOf(currentItem.getLifeValue() + " %"));

        ImageView foodPicture = (ImageView) listItemView.findViewById(R.id.fride_item_pic);
        foodPicture.setImageResource(currentItem.getPicture());


        return listItemView;
    }



}
