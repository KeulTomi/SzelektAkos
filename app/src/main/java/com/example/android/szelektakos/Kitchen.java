package com.example.android.szelektakos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class Kitchen extends Fragment {

    ArrayList<Items> fridgeItems;
    FridgeItemsAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kitchen, container, false);

        ListView fridgeItemsList = (ListView) view.findViewById(R.id.fridge_items_list);

       fridgeItems = new ArrayList<Items>();

        refreshItems();

        listAdapter = new FridgeItemsAdapter(getActivity(), fridgeItems);
        fridgeItemsList.setAdapter(listAdapter);

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
}