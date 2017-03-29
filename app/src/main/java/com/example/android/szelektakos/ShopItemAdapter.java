package com.example.android.szelektakos;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomi on 2017. 03. 26..
 */

public class ShopItemAdapter extends ArrayAdapter<Items> implements View.OnClickListener {

    private Activity activity;
    static private List<Integer> boughtItems = new ArrayList<Integer>();

    ShopItemAdapter(Activity context, ArrayList<Items> items) {
        super(context, 0, items);
        activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        final View itemView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shop_list_item, parent, false);
            TextView buyItem = (TextView) listItemView.findViewById(R.id.shop_item_buy_title);

            buyItem.setTag(position);
            buyItem.setOnClickListener(this);
            itemView = listItemView;

        }

        Items currentItem = getItem(position);

        TextView foodName = (TextView) listItemView.findViewById(R.id.shop_item_name);
        foodName.setText(currentItem.getName());

        TextView foodPrice = (TextView) listItemView.findViewById(R.id.shop_item_price);
        foodPrice.setText(String.valueOf(currentItem.getPrice()));

        TextView foodLifeValue = (TextView) listItemView.findViewById(R.id.shop_item_life_value);
        foodLifeValue.setText(String.valueOf(currentItem.getLifeValue()) + " %");

        ImageView foodPicture = (ImageView) listItemView.findViewById(R.id.shop_item_picture);
        foodPicture.setImageResource(currentItem.getPicture());

        return listItemView;
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        Items currentItem = getItem(position);

        int itemPrice = currentItem.getPrice();
        String itemName = currentItem.getName();

        if (SzelektAkos.decreasePoints(itemPrice)) {

            SzelektAkos.saveAnInteger(itemName, SzelektAkos.getAnInteger(itemName)+1);
        }

    }

    static public List<Integer> getBoughtItems() {
        return boughtItems;
    }
}
