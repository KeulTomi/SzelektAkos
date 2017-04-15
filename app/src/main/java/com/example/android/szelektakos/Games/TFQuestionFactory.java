package com.example.android.szelektakos.Games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Tomi on 2017. 04. 12..
 */

public class TFQuestionFactory {

    private static String mQuestionTf;
    private static boolean mCorrectAnswer;
    private static String[] questionsTF = {"A hulladék újrahasznosítható ", "A szemét nem hasznosítható újra", "A hulladék nem hasznosítható újra ",
            "A szemét újrahasznosítható ", "A papírt a zöld színű gyűjtőbe dobom ", "A papírt a sárga színű gyűjtőbe dobom ", "A papírt a kék színű gyűjtőbe dobom ",
    "A papírt a sárga/kék gyűjtőbe dobom ", "A  műanyagot a zöld színű gyűjtőbe dobom ", "A műanyagot a sárga színű gyűjtőbe dobom ",
    "A műanyagot a kék színű gyűjtőbe dobom ", "A műanyagot a sárga/kék gyűjtőbe dobom ", "A konzervdobozt a zöld színű gyűjtőbe dobom ",
    "A konzervdobozt a sárga színű gyűjtőbe dobom ", "A konzervdobozt a kék színű gyűjtőbe dobom ", "A konzervdobozt a sárga/kék gyűjtőbe dobom ",
    "A tejes dobozt a zöld színű gyűjtőbe dobom ", "A tejes dobozt a sárga színű gyűjtőbe dobom ", "A tejes dobozt a kék színű gyűjtőbe dobom ",
    "A tejes dobozt a sárga/kék gyűjtőbe dobom ", "A Hulladékudvarokban leadható a szemét ", "A Hulladékudvarokban leadható a veszélyes hulladék ",
    "A Hulladékudvarokba csak a lakosság vihet hulladékot ", "Nyíregyházán egy Hulladékudvar van", "Nyíregyházán két Hulladékudvar van ",
    "A zöldszigeteken leadhatóak a biohulladékok ", "A zöldszigeteken leadhatóak a veszélyes hulladékok ", "A zöldszigetek meghatározott nyitva tartás szerint üzemelnek ",
    "A hulladékudvarok meghatározott nyitva tartás szerint üzemelnek ", "A színes üveget a zöld harang alakú konténerbe dobom ",
    "A színes üveget a fehér harang alakú konténerbe dobom ", "A fehér üveget a fehér harang alakú konténerbe dobom ", "A fehér üveget a zöld harang alakú konténerbe dobom ",
    "A biohulladékból műtrágya készül ", "A biohulladékból komposzt készül", "A műanyag kvarchomokból készül ", "A műanyag kőolajból készül ",
    "Az üveg kőolajból készül ", "Az üveg kvarchomokból készül ", "Az almahéj komposztálható ", "A diólevél lassítja a komposztálást ",
    "A papírt először egyiptomiak használták ", "A papírt először kínaiak használták ", "Az üveget először Egyiptomban használták ",
    "Az üveget először Kínában használták ", "A PET palack anyaga üveg ", "Az italoskarton doboz csak papírból áll ", "A fémhulladékok hasznosítása csak külföldön lehetséges ",
    "19 000 db konzervdoboz anyagából egy komplett autó is készülhet", "A kommunális hulladék ugyanaz mint a szemét ", "A tükör újrahasznosítható ",
    "A villanykörtét a fehér harang alakú konténerbe kell dobni ", "A veszélyes hulladék leadható a Hulladékudvarokban ", "Az üveg 5 év alatt bomlik le ",
    "A használt papír zsebkendő gyűjthető szelektíven ", "A matrica nem gyűjthető szelektíven ", "A hajtógázas spray-s flakon veszélyes hulladék ",
    "A papírt kidobhatjuk a biokukába ", "A gyomot kidobhatjuk a biokukába ", "A törött üveg veszélyes hulladék "};

    private static Boolean[] correctAnswersTF = {true, true, false, false, false, true ,true, true, false, true, true ,true, false,
    true, true, true, false, true, true, true, false, true, true, false, true, false, false, false, true, true, false, true, false,
            false, true, false, true, false, true, true, true, false, true, true, false, false, false, false, true, true, false,
    false, true, false, false, true, true, false, true, false};

    private static List<String> dinamicQuestionListTF = new ArrayList<String>(Arrays.asList(questionsTF));
    private static List<Boolean> dinamicCorrectAListTF = new ArrayList<Boolean>(Arrays.asList(correctAnswersTF));

    public static void questionFactoryTF () {
        Random r = new Random();

        //Csak addig remove-olunk amíg vannak kérdések, ha elfogytak újratöltjük
        if (TFQuestionFactory.dinamicQuestionListTF.size() > 0) {

            int questionNumber = r.nextInt(TFQuestionFactory.dinamicQuestionListTF.size());

            TFQuestionFactory.mQuestionTf = TFQuestionFactory.dinamicQuestionListTF.get(questionNumber);
            dinamicQuestionListTF.remove(questionNumber);

            TFQuestionFactory.mCorrectAnswer = TFQuestionFactory.dinamicCorrectAListTF.get(questionNumber);
            dinamicCorrectAListTF.remove(questionNumber);
        }
        else {
            dinamicQuestionListTF = new ArrayList<String>(Arrays.asList(questionsTF));
            dinamicCorrectAListTF = new ArrayList<Boolean>(Arrays.asList(correctAnswersTF));
            questionFactoryTF();
        }
    }

    public static String getmQuestionTf() {
        return mQuestionTf;
    }

    public static boolean getmCorrectAnswer() {
        return mCorrectAnswer;
    }
}
