package hu.foxplan.keult.szelektakos.mainscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    ArrayList<Items> fridgeItems;
    FridgeItemsAdapter listAdapter;
    SharedPreferences mSharedPref;
    private TextView fragmentTitle;
    private ArrayList<String> ateItems = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kitchen, container, false);

        ListView fridgeItemsList = (ListView) view.findViewById(R.id.fridge_items_list);

       fridgeItems = new ArrayList<Items>();

        refreshItems();

        listAdapter = new FridgeItemsAdapter(getActivity(), fridgeItems);
        fridgeItemsList.setAdapter(listAdapter);
        fridgeItemsList.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onResume() {
        refreshItems();
        listAdapter.notifyDataSetChanged();
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

    public void refreshItems ( ) {
        fridgeItems.clear();
        for (int i = 0; i < 7; i++ ) {
            Items item = Items.innitItem(i);

            int count = SzelektAkos.getAnInteger(item.getName());

            while(count > 0) {
                fridgeItems.add(item);
                count--;
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int selectedPosition, long l) {

        // Életerő növelése
        Items items = (Items) view.getTag(); // ez volt itt előtte Items.innitItem(selectedPosition);
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