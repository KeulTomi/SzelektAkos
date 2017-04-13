package com.example.android.szelektakos;

/**
 * Created by Tomi on 2017. 03. 26..
 */

public class Items implements ListInterface{

    private String name;
    private int price;
    private int lifeValue;
    private int picture;

    private static String[] names = {"répa", "kenyér", "tej", "banán", "sajt", "pizza", "jégkrém"} ;
    private static int[] prices = {96, 102, 132, 178, 212, 482, 645};
    private static int[] lifeValues = {12, 15, 17, 23, 28, 42, 72};
    //TODO A harmadik elemet nem tölti be!!
    private static int[] pictures = {R.mipmap.shopitem01, R.mipmap.shopitem02, R.mipmap.milk, R.mipmap.shopitem04, R.mipmap.shopitem05,
            R.mipmap.shopitem06, R.mipmap.shopitem07};



    public static Items innitItem(int position) {
        Items items = new Items();

        items.name = names[position];
        items.price = prices[position];
        items.lifeValue = lifeValues[position];
        items.picture = pictures[position];

        return items;
    }
    @Override
    public String getName() {return name;}

    @Override
    public int getPrice() {return price;}

    @Override
    public int getLifeValue() {
        return lifeValue;
    }

    @Override
    public int getPicture() {
        return picture;
    }

}
