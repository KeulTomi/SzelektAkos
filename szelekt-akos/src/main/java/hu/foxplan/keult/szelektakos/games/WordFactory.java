package hu.foxplan.keult.szelektakos.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Tomi on 2017. 04. 22..
 */

public class WordFactory {
    public static String[] questions = {"Veszélyes hulladék","Ebbe tesszük a biohulladékot","Ilyen színe van a műanyag/papírhulladék gyűjtőnek"
            ,"Ilyen színű biokuka is van","Ilyen színű konténerbe tesszük a színes üveghulladékot","Ilyen színű konténerbe tesszük a fehér üveghulladékot"
            ,"Ezt a nyersanyagot is felhasználják a műanyag gyártásához","Ezt már nem tudjuk újrahasznosítani","Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe"
            ,"Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe","Ezt a hulladékot tehetjük a fehér, vagy zöld harang alakú konténerbe"
            ,"Ezt a hulladékot is átadhatjuk a Hulladékudvarokban","Ezt a hulladékot a biokukába tesszük","Ez a hulladék nem gyűjthető szelektíven"
            ,"Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe","Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe","Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe"
            ,"Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe","Fejezd be: hulladékgyűjtő …..","Tejes és gyümölcslevet töltenek bele ….. kartondoboz"
            ,"Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe","Különleges kezelést igénylő hulladék","Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe"
            ,"Veszélyes hulladék","Ilyen hulladékot tehetünk a biokukába","Ilyen üveghulladékot tehetünk a zöld harang alakú konténerbe","Veszélyes hulladék"
            ,"Ebbe tesszük a hulladékokat","Ezt tették a háztartási hulladékgyűjtő kukákba","Ő viheti be a Hulladékudvarokba  a hulladékát"
            ,"Ebbe tehető a többlet hulladék, fejezd be -  emblémás","Az ilyen ürítéseket küszöböli ki a kukába elhelyezett chip:","Ilyen színű konténerbe is elhelyezhetjük a papír/műanyag hulladékot"
            ,"Ezen az utcán van az egyik Hulladékudvar","Fejezd be: Hulladék","Ide kerülnek a hulladékok","Ő vezeti a hulladékgyűjtő autókat"
            ,"Ez készül az átválogatott hulladékból","Erre kerülnek a válogatócsarnokban a hulladékok","Ide kerül a szemét, fejezd be: Hulladéklerakó"
            ,"Ebben szerepelnek a hulladékszállítási időpontok","Minden fontos információ megtalálható rajta","Ezt a hulladékot is elhelyezhetjük a sárga/kék gyűjtőbe"
            ,"Ezeket az utcákon helyezik el, tájékoztató céllal","Ezt a fajta üveghulladékot nem helyezhetjük el a zöld szigeteken"};

    public static String[] words  = {"elem","biokuka","sárga","barna","zöld","fehér","kőolaj","szemét","papír","fém","üveg","lom"
            ,"gyom","csont","könyv","füzet","zacskó","fólia","edény","italos","karton","gumi","nejlon","olaj","kerti","színes","pala"
            ,"kuka","chip","lakos","zsák","fekete","kék","kerék","udvar","lerakó","sofőr","bála","szalag","tálca","naptár","honlap"
            ,"flakon","plakát","ablak"};

    public static String mCurrentWord;
    public static String mCurrentQuestion;

    public static List<String> dinamicQuestionList = new ArrayList<String>(Arrays.asList(questions));
    public static List<String> dinamicWordList = new ArrayList<String>(Arrays.asList(words));

    public static void innitWordAndQuestions(){
        Random r = new Random();

        //Csak addig remove-olunk amíg vannak szemetek, ha elfogytak újratöltjük
        if (WordFactory.dinamicQuestionList.size() > 0) {

            int questionNumber = r.nextInt(dinamicQuestionList.size());

            mCurrentQuestion = dinamicQuestionList.get(questionNumber);
            dinamicQuestionList.remove(questionNumber);

            mCurrentWord = dinamicWordList.get(questionNumber);
            dinamicWordList.remove(questionNumber);
        }
        else {
            dinamicQuestionList = new ArrayList<String>(Arrays.asList(questions));
            dinamicWordList = new ArrayList<String>(Arrays.asList(words));
        }
    }
}
