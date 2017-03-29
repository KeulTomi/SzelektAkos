package com.example.android.szelektakos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomi on 2017. 03. 25..
 */

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO Megvett elemek vizsgálata és bele töltése a hűtőbe
    private List<Integer> boughtItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        ListView itemList = (ListView) findViewById(R.id.shop_list);
        ImageView closeShop = (ImageView) findViewById(R.id.close_shop);

        closeShop.setOnClickListener(this);

        ArrayList<Items> listItems = new ArrayList<Items>();
        for (int i = 0; i < 7; i++ ) {
            listItems.add(Items.innitItem(i));

        }

        ShopItemAdapter listAdapter = new ShopItemAdapter(this,listItems);
        itemList.setAdapter(listAdapter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.close_shop:
                finish();

        }

    }
}
