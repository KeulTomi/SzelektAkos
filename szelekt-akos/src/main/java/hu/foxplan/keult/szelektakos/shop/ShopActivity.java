package hu.foxplan.keult.szelektakos.shop;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.SzelektAkos;
import hu.foxplan.keult.szelektakos.mainscreen.MainActivity;

/**
 * Created by Tomi on 2017. 03. 25..
 */

public class ShopActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    ListView itemList;
    ImageView closeShop;
    TextView foodTopic;
    TextView trousersTopic;
    TextView plusTopic;
    View headerView;
    //TODO Megvett elemek vizsgálata és bele töltése a hűtőbe
    private List<Integer> boughtItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hu.foxplan.keult.szelektakos.R.layout.shop);

        LayoutInflater inflater = this.getLayoutInflater();

        itemList = (ListView) findViewById(hu.foxplan.keult.szelektakos.R.id.shop_list);
        closeShop = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.close_shop);

        ArrayList<Items> listFoodItems = new ArrayList<Items>();
        for (int i = 0; i < 7; i++ ) {
            listFoodItems.add(Items.innitItem(i));

        }

        ShopItemAdapter listAdapterFood = new ShopItemAdapter(this,listFoodItems);
        headerView = inflater.inflate(
                R.layout.shop_list_header, null);
        itemList.addHeaderView(headerView, null, false);
        itemList.setAdapter(listAdapterFood);

        foodTopic = (TextView) headerView.findViewById(hu.foxplan.keult.szelektakos.R.id.shop_food_topic);
        trousersTopic = (TextView) headerView.findViewById(hu.foxplan.keult.szelektakos.R.id.shop_trousers_topic);
        plusTopic = (TextView) headerView.findViewById(hu.foxplan.keult.szelektakos.R.id.shop_plus_topic);

        closeShop.setOnClickListener(this);
        foodTopic.setOnClickListener(this);
        trousersTopic.setOnClickListener(this);
        plusTopic.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case hu.foxplan.keult.szelektakos.R.id.close_shop:
                SzelektAkos.saveTroousersToMainData(TrouserAdapter.isBoughtTrouser);
                finish();
                overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_down);
                MainActivity.uiHandler.sendEmptyMessage(MainActivity.MSG_RESTART_ACTIVITY);
                break;

            case hu.foxplan.keult.szelektakos.R.id.shop_food_topic:

                ArrayList<Items> listFoodItems = new ArrayList<Items>();
                for (int i = 0; i < 7; i++ ) {
                    listFoodItems.add(Items.innitItem(i));

                }

                foodTopic.setTextColor(Color.WHITE);
                trousersTopic.setTextColor(Color.parseColor("#c2c2c2"));
                plusTopic.setTextColor(Color.parseColor("#c2c2c2"));
                ShopItemAdapter listAdapterFood = new ShopItemAdapter(this,listFoodItems);
                itemList.setAdapter(listAdapterFood);
                break;

            case hu.foxplan.keult.szelektakos.R.id.shop_trousers_topic:
                ArrayList<ItemsForTrouser> listTrousersItems = new ArrayList<ItemsForTrouser>();
                for (int i = 0; i < 7; i++ ) {
                    listTrousersItems.add(ItemsForTrouser.innitItem(i));

                }

                foodTopic.setTextColor(Color.parseColor("#c2c2c2"));
                trousersTopic.setTextColor(Color.WHITE);
                plusTopic.setTextColor(Color.parseColor("#c2c2c2"));
                TrouserAdapter listAdapterTrouser = new TrouserAdapter(this,listTrousersItems);
                itemList.setAdapter(listAdapterTrouser);
                break;

            case hu.foxplan.keult.szelektakos.R.id.shop_plus_topic:
                ArrayList<ItemsForPlus> listPlusItems = new ArrayList<ItemsForPlus>();
                for (int i = 0; i < 3; i++ ) {
                    listPlusItems.add(ItemsForPlus.innitItem(i));

                }

                foodTopic.setTextColor(Color.parseColor("#c2c2c2"));
                trousersTopic.setTextColor(Color.parseColor("#c2c2c2"));
                plusTopic.setTextColor(Color.WHITE);
                PlusAdapter listAdapterPlus = new PlusAdapter(this,listPlusItems);
                itemList.setAdapter(listAdapterPlus);
                break;

        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        foodTopic.setOnClickListener(this);
        trousersTopic.setOnClickListener(this);
        plusTopic.setOnClickListener(this);


        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return false;
    }
}
