package com.example.android.szelektakos.shop;

import com.example.android.szelektakos.ListInterface;
import com.example.android.szelektakos.R;
import com.example.android.szelektakos.SzelektAkos;

/**
 * Created by Tomi on 2017. 03. 29..
 */

public class ItemsForTrouser implements ListInterface {

    public static String[] names = {"THG", "Kék", "Bordó", "Sárga", "Neon", "Kockás", "Csíkos"} ;
    public static int[] prices = {0, 77, 165, 213, 562, 788, 802,};
    public static int[] pictures = {R.mipmap.pants00, R.mipmap.pants01, R.mipmap.pants02, R.mipmap.pants03, R.mipmap.pants04,
            R.mipmap.pants05, R.mipmap.pants06};
    public String name;
    public int price;
    public int lifeValue;
    public int picture;

    public static ItemsForTrouser innitItem(int position) {
        ItemsForTrouser items = new ItemsForTrouser();

        SzelektAkos.saveABoolean(names[0], true);

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
