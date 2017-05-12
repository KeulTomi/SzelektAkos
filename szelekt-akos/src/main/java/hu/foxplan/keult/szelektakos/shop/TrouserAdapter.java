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
 * Created by Tomi on 2017. 03. 29..
 */

public class TrouserAdapter extends ArrayAdapter<ItemsForTrouser> implements View.OnClickListener {

    public static boolean[] isBoughtTrouser;
    private Activity activity;
    public ListView foodList;
    public int realPosition;

    TrouserAdapter(Activity context, ArrayList<ItemsForTrouser> items) {
        super(context, 0, items);
        activity = context;
        isBoughtTrouser = ItemsForTrouser.boughtTrousers;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        foodList = (ListView) parent.findViewById(R.id.shop_list);
        //foodList.setFastScrollEnabled(true);

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shop_list_item, parent, false);

            TextView buyItem = (TextView) listItemView.findViewById(R.id.shop_item_buy_title);

            buyItem.setTag(position);
            buyItem.setOnClickListener(this);

            /*ItemsForTrouser currentItem = getItem(position);

            ItemsForTrouser.innitItem(position);

            positionManual = position;


            isBoughtTrouser = SzelektAkos.mainBoughtTrousersList;
            isBoughtTrouser[0] = true;


            lifeValue.setVisibility(View.INVISIBLE);

            itemView = listItemView;*/

        }

        ItemsForTrouser currentItem = getItem(position);


        TextView foodName = (TextView) listItemView.findViewById(R.id.shop_item_name);
        foodName.setText(currentItem.getName());

        TextView foodPrice = (TextView) listItemView.findViewById(R.id.shop_item_price);
        foodPrice.setText(String.valueOf(currentItem.getPrice()));

        ImageView foodPicture = (ImageView) listItemView.findViewById(R.id.shop_item_picture);
        foodPicture.setImageResource(currentItem.getPicture());

        setButtonText(listItemView, position);

        return listItemView;
    }

    @Override
    public void onClick(final View view) {

//        final int position = (Integer) view.getTag();
        realPosition = foodList.getPositionForView((View) view.getParent()) - 1;
        final ItemsForTrouser currentItem = getItem(realPosition);

        switch (view.getId()) {

            case R.id.shop_item_buy_title:
                if (isBoughtTrouser[realPosition] == false) {
                    //show dialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Biztosan meg szeretnéd venni?")
                            .setPositiveButton("Megveszem", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    int itemPrice = currentItem.getPrice();
                                    String itemName = currentItem.getName();

                                    if (SzelektAkos.decreasePoints(itemPrice)) {

                                        isBoughtTrouser[realPosition] = true;
                                        setButtonText(view, realPosition);
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
                } else {
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
                break;
        }
    }

    public void setButtonText(View listItemView, int position) {

        TextView buttonText = (TextView) listItemView.findViewById(R.id.shop_item_buy_title);

        if (isBoughtTrouser[position] == false)
            buttonText.setText("megvesz");
        else
            buttonText.setText("hord");
    }
}
