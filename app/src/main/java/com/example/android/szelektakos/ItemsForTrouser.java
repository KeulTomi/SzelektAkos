package com.example.android.szelektakos;

/**
 * Created by Tomi on 2017. 03. 29..
 */

public class ItemsForTrouser implements ListInterface {

    private String name;
    private int price;
    private int lifeValue;
    private int picture;

    private static String[] names = {"THG", "Kék", "Bordó", "Sárga", "Neon", "Kockás", "Csíkos"} ;
    private static int[] prices = {0, 77, 165, 213, 562, 788, 802,};
    private static int[] pictures = {R.mipmap.pants00, R.mipmap.pants01, R.mipmap.pants02, R.mipmap.pants03, R.mipmap.pants04,
            R.mipmap.pants05, R.mipmap.pants06};


    @Override
    public Object innitItem(int position) {
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
