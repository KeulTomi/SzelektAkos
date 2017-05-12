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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.SzelektAkos;

/**
 * Created by Tomi on 2017. 03. 26..
 */

public class ShopItemAdapter extends ArrayAdapter<Items> implements View.OnClickListener {

    private Activity activity;
    private TextView buyItem;
    public ListView foodList;
    public int realPosition;

    ShopItemAdapter(Activity context, ArrayList<Items> items) {
        super(context, 0, items);
        activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        final View itemView;
        foodList = (ListView) parent.findViewById(R.id.shop_list);

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(hu.foxplan.keult.szelektakos.R.layout.shop_list_item, parent, false);
            buyItem = (TextView) listItemView.findViewById(hu.foxplan.keult.szelektakos.R.id.shop_item_buy_title);

            buyItem.setTag(position);
            buyItem.setOnClickListener(this);
            itemView = listItemView;

        }

        Items currentItem = getItem(position);

        TextView foodName = (TextView) listItemView.findViewById(hu.foxplan.keult.szelektakos.R.id.shop_item_name);
        foodName.setText(currentItem.getName());

        TextView foodPrice = (TextView) listItemView.findViewById(hu.foxplan.keult.szelektakos.R.id.shop_item_price);
        foodPrice.setText(String.valueOf(currentItem.getPrice()));

        TextView foodLifeValue = (TextView) listItemView.findViewById(hu.foxplan.keult.szelektakos.R.id.shop_item_life_value);
        foodLifeValue.setText(String.valueOf(currentItem.getLifeValue()) + " %");

        ImageView foodPicture = (ImageView) listItemView.findViewById(hu.foxplan.keult.szelektakos.R.id.shop_item_picture);
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

                        realPosition = foodList.getPositionForView((View) view.getParent()) - 1;
                        Items currentItem = getItem(realPosition);

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
