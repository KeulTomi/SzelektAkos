package com.example.android.szelektakos.shop;

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

import com.example.android.szelektakos.R;
import com.example.android.szelektakos.SzelektAkos;

import java.util.ArrayList;

/**
 * Created by Tomi on 2017. 03. 29..
 */

public class TrouserAdapter extends ArrayAdapter<ItemsForTrouser> implements View.OnClickListener {

    TextView buyItem;
    private Activity activity;
    ItemsForTrouser currentItem;
    public static boolean[] isBoughtTrouser;
    public int positionManual;

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

            ItemsForTrouser.innitItem(position);

            positionManual = position;


            isBoughtTrouser = SzelektAkos.mainBoughtTrousersList;
            isBoughtTrouser[0] = true;

            if(isBoughtTrouser[position] == false) {
                buyItem.setText("megvesz");
            }

            findTheBought(position, buyItem);

            lifeValue.setVisibility(View.INVISIBLE);
            buyItem.setTag(position);
            buyItem.setOnClickListener(this);
            itemView = listItemView;

        }

        currentItem = getItem(position);


        TextView foodName = (TextView) listItemView.findViewById(R.id.shop_item_name);
        foodName.setText(currentItem.getName());

        TextView foodPrice = (TextView) listItemView.findViewById(R.id.shop_item_price);
        foodPrice.setText(String.valueOf(currentItem.getPrice()));

        ImageView foodPicture = (ImageView) listItemView.findViewById(R.id.shop_item_picture);
        foodPicture.setImageResource(currentItem.getPicture());

        return listItemView;
    }

    @Override
    public void onClick(final View view) {

        if (isBoughtTrouser[positionManual] == false) {
            //show dialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Biztosan meg szeretnéd venni?")
                    .setPositiveButton("Megveszem", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            int position = (Integer) view.getTag();
                            final ItemsForTrouser currentItem = getItem(position);

                            int itemPrice = currentItem.getPrice();
                            String itemName = currentItem.getName();

                            if (SzelektAkos.decreasePoints(itemPrice)) {

                                isBoughtTrouser[position] = true;
                                buyItem.setText("hord");
                            }
                            return;


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
        else{
            //show dialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Biztosan fel szeretnéd venni ezt a nadrágot?")
                    .setPositiveButton("Felveszem", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            SzelektAkos.changeTrouser(currentItem.getPicture());
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

    public void findTheBought (int position, TextView buyItem1){

        if (isBoughtTrouser[position] == true) {
            buyItem1.setText("hord");
        }
    }

}
