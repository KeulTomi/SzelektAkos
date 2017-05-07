package hu.foxplan.keult.szelektakos.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hu.foxplan.keult.szelektakos.R;

/**
 * Created by Tomi on 2017. 04. 15..
 */

public class TrashesFactory {

    public static int[] picturesOfTrashes = {R.drawable.trash_blue, R.drawable.trash_braun, R.drawable.trash_build, R.drawable.trash_green,
            R.drawable.trash_white, R.drawable.trash_yellow};
    public static String[] stringsOfMipmaps = {"trash_blue", "trash_braun", "trash_build", "trash_green", "trash_white", "trash_yellow"};
    public static Integer[] picturesOfGarbages = {R.drawable.pill, R.drawable.battery, R.drawable.book, R.drawable.bottle_colorful,
            R.drawable.bottle_white, R.drawable.bottlecap, R.drawable.butter, R.drawable.can, R.drawable.chips, R.drawable.electronics,
            R.drawable.home_oil, R.drawable.oil, R.drawable.glass, R.drawable.plant, R.drawable.plant2, R.drawable.plant3, R.drawable.paper,
            R.drawable.paper_bag, R.drawable.newspaper, R.drawable.ligth_bulb, R.drawable.shower_gel, R.drawable.simple_can, R.drawable.yogurt,
            R.drawable.plastic, R.drawable.cornflakes};
    public static Integer[] correctNumbersArray = {2, 2, 0, 3, 4, 5, 5, 5, 2, 2, 2, 2, 4, 1, 1, 1, 0, 0, 0, 2, 5, 5, 5, 5, 5};
    public static int mCorrectPicture;
    public static int mCorrectPicture2;
    public static String mCorrectPictureString;
    public static int mCurrentGarbage;
    public static int mNumberOfCorrectPicture;

    public static List<Integer> dinamicGarbagePicList = new ArrayList<Integer>(Arrays.asList(picturesOfGarbages));
    public static List<Integer> dinamicNumbersList = new ArrayList<Integer>(Arrays.asList(correctNumbersArray));

    public static void innitTrashes() {
        Random r = new Random();

        //Csak addig remove-olunk amíg vannak szemetek, ha elfogytak újratöltjük
        if (TrashesFactory.dinamicGarbagePicList.size() > 0) {

            int questionNumber = r.nextInt(TrashesFactory.dinamicGarbagePicList.size());

            TrashesFactory.mCurrentGarbage = TrashesFactory.dinamicGarbagePicList.get(questionNumber);
            dinamicGarbagePicList.remove(questionNumber);

            TrashesFactory.mNumberOfCorrectPicture = TrashesFactory.dinamicNumbersList.get(questionNumber);
            dinamicNumbersList.remove(questionNumber);

            TrashesFactory.mCorrectPicture = picturesOfTrashes[TrashesFactory.mNumberOfCorrectPicture];
            TrashesFactory.mCorrectPictureString = stringsOfMipmaps[TrashesFactory.mNumberOfCorrectPicture];
            TrashesFactory.mCorrectPicture2 = mCorrectPicture;

            if (TrashesFactory.mNumberOfCorrectPicture == 0) {
                //Ha kék a correctPicture, akkor a sárgát rakjuk a másikba
                TrashesFactory.mCorrectPicture2 = picturesOfTrashes[5];
            }

            if (TrashesFactory.mNumberOfCorrectPicture == 5) {
                //Ha sárga a correctPicture, akkor a kéket rakjuk a másikba
                TrashesFactory.mCorrectPicture2 = picturesOfTrashes[0];
            }

        } else {
            dinamicGarbagePicList = new ArrayList<Integer>(Arrays.asList(picturesOfGarbages));
            dinamicNumbersList = new ArrayList<Integer>(Arrays.asList(correctNumbersArray));
            innitTrashes();
        }
    }
}
