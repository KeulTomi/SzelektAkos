package com.example.android.szelektakos.shop;

import com.example.android.szelektakos.ListInterface;
import com.example.android.szelektakos.R;

/**
 * Created by Tomi on 2017. 03. 29..
 */

public class ItemsForTrouser implements ListInterface {

    public static String[] names = {"THG", "Kék", "Bordó", "Sárga", "Neon", "Kockás", "Csíkos"} ;
    public static int[] prices = {0, 77, 165, 213, 562, 788, 802,};
    public static boolean[] boughtTrousers = {true, false, false, false, false, false, false};
    public static int[] pictures = {R.mipmap.pants_thg, R.mipmap.pants_blue, R.mipmap.pants_red, R.mipmap.pants_yellow, R.mipmap.pants_neon,
            R.mipmap.pants_kockas, R.mipmap.pants_csikos};
    public String name;
    public int price;
    public int lifeValue;
    public int picture;

    public static ItemsForTrouser innitItem(int position) {
        ItemsForTrouser items = new ItemsForTrouser();

        items.name = names[position];
        items.price = prices[position];
        items.picture = pictures[position];

        return items;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getLifeValue() {
        return 0;
    }

    @Override
    public int getPicture() {
        return picture;
    }
}
