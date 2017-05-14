package hu.foxplan.keult.szelektakos.mainscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.SzelektAkos;
import hu.foxplan.keult.szelektakos.shop.Items;

public class Kitchen extends Fragment implements AdapterView.OnItemClickListener {

    public static Handler kitchenHandler;
    ArrayList<Items> fridgeItems;
    FridgeItemsAdapter listAdapter;
    ListView fridgeItemsList;
    SharedPreferences mSharedPref;
    private TextView fragmentTitle;
    private ArrayList<String> ateItems = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kitchen, container, false);

        fridgeItemsList = (ListView) view.findViewById(R.id.fridge_items_list);

        fridgeItems = new ArrayList<Items>();
        kitchenHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                fridgeItems.add((Items) msg.obj);
                listAdapter.notifyDataSetChanged();
            }
        };

        refreshItems(false);

        listAdapter = new FridgeItemsAdapter(getActivity(), fridgeItems);
        fridgeItemsList.setAdapter(listAdapter);

        fridgeItemsList.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        //refreshItems();
        //listAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
//        int index = 0;
//        while (index != ateItems.size() - 1){
//            mSharedPref.edit().remove(ateItems.get(index));
//            mSharedPref.edit().apply();
//            listAdapter.notifyDataSetChanged();
//            index++;
//        }
        super.onDestroyView();
    }

    public void refreshItems(boolean clearItems) {

        if (clearItems) fridgeItems.clear();

        for (int i = 0; i < 7; i++ ) {
            Items item = Items.innitItem(i);

            int count = SzelektAkos.getAnInteger(item.getName());

            while(count > 0) {
                fridgeItems.add(item);
                count--;
            }
        }
    }

    private void removeDummyItems(int numOfDummies) {

        int itemsToKeep = fridgeItems.size() - numOfDummies;

        for (int i = 0; i < numOfDummies; i++) {
            fridgeItems.remove(i);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int selectedPosition, long l) {

        // Életerő növelése
        Items items = (Items) view.getTag();
        SzelektAkos.changeLifeValue(items.getLifeValue());

        // Elem eltávolítása a hűtőből
        fridgeItems.remove(selectedPosition);
        listAdapter.notifyDataSetChanged();

        // Megvásárolt elemek számának csökkentése a sharedpreferences-ben
        //
        int numOfBoughtItems = SzelektAkos.getAnInteger(items.getName());

        numOfBoughtItems--; // Elemszám csökkentése
        if (numOfBoughtItems == 0) {
            // Ha nulla lett az elemszám (azaz nincs több belőle a hűtőben) akkor a kulcs törlése
            mSharedPref = getActivity().getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
            mSharedPref.edit().remove(items.getName()).apply();
        } else {
            // Ha még maradt az adott termékből a hűtőben akkor mentés SharedPref-ben
            SzelektAkos.saveAnInteger(items.getName(), numOfBoughtItems);
        }

    }

}