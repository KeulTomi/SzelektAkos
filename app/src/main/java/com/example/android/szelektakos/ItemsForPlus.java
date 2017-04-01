package com.example.android.szelektakos;

/**
 * Created by Tomi on 2017. 03. 30..
 */

public class ItemsForPlus implements ListInterface {

    private String name;
    private int price;
    private int lifeValue;
    private int picture;

    private static String[] names = {"Energia", "Étel", "Energia+Étel"} ;
    private static int[] prices = {762, 865, 1472};
    private static int[] lifeValues = {100, 100, 100};
    //TODO A harmadik elemet nem tölti be!!
    private static int[] pictures = {R.mipmap.plusenergy, R.mipmap.plusfood, R.mipmap.plusfoodenergy};



    public static ItemsForPlus innitItem(int position) {
        ItemsForPlus items = new ItemsForPlus();

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

