package hu.foxplan.keult.szelektakos.shop;

import hu.foxplan.keult.szelektakos.ListInterface;
import hu.foxplan.keult.szelektakos.R;

/**
 * Created by Tomi on 2017. 03. 26..
 */

public class Items implements ListInterface {

    private static String[] names = {"répa", "kenyér", "tej", "banán", "sajt", "pizza", "jégkrém"} ;
    private static int[] prices = {96, 102, 132, 178, 212, 482, 645};
    private static int[] lifeValues = {12, 15, 17, 23, 28, 42, 72};
    //TODO A harmadik elemet nem tölti be!!
    private static int[] pictures = {R.drawable.shopitem01, R.drawable.shopitem02, R.drawable.milk, R.drawable.shopitem04, R.drawable.shopitem05,
            R.drawable.shopitem06, R.drawable.shopitem07};
    private String name;
    private int price;
    private int lifeValue;
    private int picture;

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
