package hu.foxplan.keult.szelektakos.shop;

import hu.foxplan.keult.szelektakos.ListInterface;
import hu.foxplan.keult.szelektakos.R;

/**
 * Created by Tomi on 2017. 03. 29..
 */

public class ItemsForTrouser implements ListInterface {

    public static String[] names = {"THG", "Kék", "Bordó", "Sárga", "Neon", "Kockás", "Csíkos"} ;
    public static int[] prices = {0, 77, 165, 213, 562, 788, 802,};
    public static boolean[] boughtTrousers = {true, false, false, false, false, false, false};
    public static int[] pictures = {R.drawable.pants00, R.drawable.pants_blue, R.drawable.pants_red, R.drawable.pants_yellow, R.drawable.pants_neon,
            R.drawable.pants_kockas, R.drawable.pants_csikos};
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
