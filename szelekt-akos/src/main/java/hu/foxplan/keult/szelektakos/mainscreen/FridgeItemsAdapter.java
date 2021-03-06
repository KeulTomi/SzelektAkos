package hu.foxplan.keult.szelektakos.mainscreen;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.shop.Items;

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
        listItemView.setTag(getItem(position));

        TextView foodLifeValue = (TextView) listItemView.findViewById(R.id.fridge_item_value);
        foodLifeValue.setText(String.valueOf(currentItem.getLifeValue() + " %"));

        ImageView foodPicture = (ImageView) listItemView.findViewById(R.id.fride_item_pic);
        foodPicture.setImageResource(currentItem.getPicture());
        foodPicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


        return listItemView;
    }

}
