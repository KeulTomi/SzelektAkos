package com.example.android.szelektakos.Shop;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.szelektakos.R;
import com.example.android.szelektakos.SzelektAkos;

import java.util.ArrayList;

/**
 * Created by Tomi on 2017. 03. 29..
 */

public class TrouserAdapter extends ArrayAdapter<ItemsForTrouser> implements View.OnClickListener {

    private Activity activity;
    TextView buyItem;

    TrouserAdapter(Activity context, ArrayList<ItemsForTrouser> items) {
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
            buyItem = (TextView) listItemView.findViewById(R.id.shop_item_buy_title);
            TextView lifeValue = (TextView) listItemView.findViewById(R.id.shop_item_life_value);

            ItemsForTrouser currentItem = getItem(position);

            if (SzelektAkos.getABoolean(currentItem.getName())) {
                buyItem.setText("hord");
            }

            lifeValue.setVisibility(View.INVISIBLE);
            buyItem.setTag(position);
            buyItem.setOnClickListener(this);
            itemView = listItemView;

        }

        ItemsForTrouser currentItem = getItem(position);

        TextView foodName = (TextView) listItemView.findViewById(R.id.shop_item_name);
        foodName.setText(currentItem.getName());

        TextView foodPrice = (TextView) listItemView.findViewById(R.id.shop_item_price);
        foodPrice.setText(String.valueOf(currentItem.getPrice()));

        ImageView foodPicture = (ImageView) listItemView.findViewById(R.id.shop_item_picture);
        foodPicture.setImageResource(currentItem.getPicture());

        return listItemView;
    }

    @Override
    public void onClick(View view) {

        int position = (Integer) view.getTag();
        ItemsForTrouser currentItem = getItem(position);

        if (buyItem.getText() == "megvesz") {

            int itemPrice = currentItem.getPrice();
            String itemName = currentItem.getName();

            if (SzelektAkos.decreasePoints(itemPrice)) {

                SzelektAkos.saveABoolean(itemName, true);
            }
        }
        else SzelektAkos.changeTrouser(currentItem.getPicture());
    }
}
