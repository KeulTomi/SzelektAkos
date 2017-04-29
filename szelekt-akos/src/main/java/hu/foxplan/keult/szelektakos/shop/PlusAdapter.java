package hu.foxplan.keult.szelektakos.shop;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.SzelektAkos;

/**
 * Created by Tomi on 2017. 03. 30..
 */

public class PlusAdapter extends ArrayAdapter<ItemsForPlus> implements View.OnClickListener {

    static private List<Integer> boughtItems = new ArrayList<Integer>();
    private Activity activity;

    PlusAdapter(Activity context, ArrayList<ItemsForPlus> items) {
        super(context, 0, items);
        activity = context;
    }

    static public List<Integer> getBoughtItems() {
        return boughtItems;
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

        ItemsForPlus currentItem = getItem(position);

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
    public void onClick(final View view) {

                //show dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Biztosan meg szeretnéd venni?")
                        .setPositiveButton("Megveszem", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int position = (Integer) view.getTag();
                                ItemsForPlus currentItem = getItem(position);

                                int itemPrice = currentItem.getPrice();
                                String itemName = currentItem.getName();

                                if (SzelektAkos.decreasePoints(itemPrice)) {

                                    SzelektAkos.saveAnInteger(itemName, SzelektAkos.getAnInteger(itemName)+1);
                                }
                            }
                        });

                    builder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }
}

