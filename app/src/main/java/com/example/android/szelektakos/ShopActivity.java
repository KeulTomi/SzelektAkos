package com.example.android.szelektakos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomi on 2017. 03. 25..
 */

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO Megvett elemek vizsgálata és bele töltése a hűtőbe
    private List<Integer> boughtItems = new ArrayList<>();
    ListView itemList;
    ImageView closeShop;
    TextView foodTopic;
    TextView trousersTopic;
    TextView plusTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        itemList = (ListView) findViewById(R.id.shop_list);
        closeShop = (ImageView) findViewById(R.id.close_shop);
        foodTopic = (TextView) findViewById(R.id.shop_food_topic);
        trousersTopic = (TextView) findViewById(R.id.shop_trousers_topic);
        plusTopic = (TextView) findViewById(R.id.shop_plus_topic);

        closeShop.setOnClickListener(this);
        foodTopic.setOnClickListener(this);
        trousersTopic.setOnClickListener(this);
        plusTopic.setOnClickListener(this);

        ArrayList<Items> listFoodItems = new ArrayList<Items>();
        for (int i = 0; i < 7; i++ ) {
            listFoodItems.add(Items.innitItem(i));

        }

        ShopItemAdapter listAdapterFood = new ShopItemAdapter(this,listFoodItems);
        itemList.setAdapter(listAdapterFood);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.close_shop:
                finish();
                break;

            case R.id.shop_food_topic:

                ArrayList<Items> listFoodItems = new ArrayList<Items>();
                for (int i = 0; i < 7; i++ ) {
                    listFoodItems.add(Items.innitItem(i));

                }

                ShopItemAdapter listAdapterFood = new ShopItemAdapter(this,listFoodItems);
                itemList.setAdapter(listAdapterFood);
                break;

            case R.id.shop_trousers_topic:
                ArrayList<ItemsForTrouser> listTrousersItems = new ArrayList<ItemsForTrouser>();
                for (int i = 0; i < 7; i++ ) {
                    listTrousersItems.add(ItemsForTrouser.innitItem(i));

                }

                TrouserAdapter listAdapterTrouser = new TrouserAdapter(this,listTrousersItems);
                itemList.setAdapter(listAdapterTrouser);
                break;

            case R.id.shop_plus_topic:
                ArrayList<ItemsForPlus> listPlusItems = new ArrayList<ItemsForPlus>();
                for (int i = 0; i < 3; i++ ) {
                    listPlusItems.add(ItemsForPlus.innitItem(i));

                }

                PlusAdapter listAdapterPlus = new PlusAdapter(this,listPlusItems);
                itemList.setAdapter(listAdapterPlus);
                break;

        }

    }
}
