package com.example.android.szelektakos;

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

public class Kitchen extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<Items> fridgeItems;
    FridgeItemsAdapter listAdapter;
    SharedPreferences mSharedPref;
    private TextView fragmentTitle;

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

        Items item = new Items();
        //TODO Itt kell hozzá adni az életéhez a changeLifeValue() metódussal!!
        SzelektAkos.changeLifeValue(item.getLifeValue());
        mSharedPref = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        Items items = Items.innitItem(selectedPosition);
        //TODO Itt kell hozzá adni az életéhez a increaseLife() metódussal!!
        SzelektAkos.changeLifeValue(items.getLifeValue());
        fridgeItems.remove(selectedPosition);
        mSharedPref.edit().remove(items.getName());
        mSharedPref.edit().apply();
        listAdapter.notifyDataSetChanged();
    }

}