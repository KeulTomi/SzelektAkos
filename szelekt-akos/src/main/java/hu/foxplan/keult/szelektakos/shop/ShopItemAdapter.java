package hu.foxplan.keult.szelektakos.shop;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Message;
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
import hu.foxplan.keult.szelektakos.mainscreen.Kitchen;

/**
 * Created by Tomi on 2017. 03. 26..
 */

public class ShopItemAdapter extends ArrayAdapter<Items> implements View.OnClickListener {

    public ListView foodList;
    public int realPosition;
    private Activity activity;
    private TextView buyItem;

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
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shop_list_item, parent, false);

            listItemView.findViewById(R.id.shop_item_buy_title).setOnClickListener(this);
            itemView = listItemView;

        }

        Items currentItem = getItem(position);
        listItemView.findViewById(R.id.shop_item_buy_title).setTag(position);

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

                        //realPosition = foodList.getPositionForView((View) view.getParent()) - 1;
                        int position = (Integer) view.getTag();
                        Items currentItem = getItem(position);

                        int itemPrice = currentItem.getPrice();
                        String itemName = currentItem.getName();

                        if (SzelektAkos.decreasePoints(itemPrice)) {

                            SzelektAkos.saveAnInteger(itemName, SzelektAkos.getAnInteger(itemName)+1);
                            Message msg = new Message();
                            msg.obj = currentItem;
                            Kitchen.kitchenHandler.sendMessage(msg);
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
