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

    public static int[] picturesOfTrashes = {R.mipmap.trash_blue, R.mipmap.trash_braun, R.mipmap.trash_build, R.mipmap.trash_green,
            R.mipmap.trash_white, R.mipmap.trash_yellow};
    public static String[] stringsOfMipmaps = {"trash_blue", "trash_braun", "trash_build", "trash_green", "trash_white", "trash_yellow"};
    public static Integer[] picturesOfGarbages = {R.mipmap.pill, R.mipmap.battery, R.mipmap.book, R.mipmap.bottle_colorful,
            R.mipmap.bottle_white, R.mipmap.bottlecap, R.mipmap.butter, R.mipmap.can, R.mipmap.chips, R.mipmap.electronics,
            R.mipmap.home_oil, R.mipmap.oil, R.mipmap.glass, R.mipmap.plant, R.mipmap.plant2, R.mipmap.plant3, R.mipmap.paper,
            R.mipmap.paper_bag, R.mipmap.newspaper, R.mipmap.ligth_bulb, R.mipmap.shower_gel, R.mipmap.simple_can, R.mipmap.yogurt,
            R.mipmap.plastic, R.mipmap.cornflakes};
    public static Integer[] correctNumbersArray = {2, 2, 0, 3, 4, 5, 5, 5, 2, 2, 2, 2, 4, 1, 1, 1, 0, 0, 0, 2, 5, 5, 5, 5, 5};
    public static int mCorrectPicture;
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
        } else {
            dinamicGarbagePicList = new ArrayList<Integer>(Arrays.asList(picturesOfGarbages));
            dinamicNumbersList = new ArrayList<Integer>(Arrays.asList(correctNumbersArray));
            innitTrashes();
        }
    }
}
