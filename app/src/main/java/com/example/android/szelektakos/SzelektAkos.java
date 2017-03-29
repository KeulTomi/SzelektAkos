package com.example.android.szelektakos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Created by Tomi on 2017. 03. 28..
 */

public class SzelektAkos extends Application {

    private static int points;
    private static int energy;
    private static int life;
    private static int trouserToWear;
    private static List<Integer> avaiableTrousers = new ArrayList<>();
    private static SharedPreferences mSharedPref;
    private static Context appContext;

    public static void innitApp(Context context) {
        appContext = context;
        getAllPrefs();
    }

    public static boolean decreasePoints (int minusPoints) {
        if (points-minusPoints >= 0){
            points = points-minusPoints;
            return true;
        }
        else {
            Toast.makeText(appContext,"Sajnos nincs elég pontod",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static void increasePoints (int plusPoints) {
        points = points+plusPoints;
    }

    public static String getPoints () {
        return String.valueOf(points);
    }

    public static void decreaseEnergy (int minusEnergy) {
        energy = (energy-minusEnergy >= 0 ? energy-minusEnergy: 0);
//        if(energy-minusEnergy>=0) {
//            energy -= minusEnergy;
//        }
//        else {
//            energy = 0;
//        }
    }

    public static void increaseEnergy (int plusEnergy) {
        energy = (energy+plusEnergy<= 100 ? energy+plusEnergy: 100);
//        if (energy+plusEnergy <= 100) {
//            energy = energy + plusEnergy;
//        }
//        else {
//            energy = 100;
//        }
    }

    public static int getEnergy(){
        return energy;
    }

    public static int getLife() {
        return life;
    }

    public static void decreaseLife (int minusLife) {
        life = (life-minusLife >= 0 ? life-minusLife : 0);
//        if(life-minusLife >= 0) {
//            life -= minusLife;
//        }
//        else {
//            life = 0;
//        }
    }

    public static void increaseLife(int plusLife) {
        life = (life+plusLife<= 100 ? life+plusLife : 100);
//        if (life+plusLife <= 100){
//            life = life+plusLife;
//        }
//        else {
//            life = 100;
//        }
    }

    public static void changeTrouser (int changeToThis) {
        trouserToWear = changeToThis;
    }

    public static int getTrouserToWearRes() {
        //TODO OnResume()-be frissíteni kell mindig a resource-t
        return trouserToWear;
    }

    public static void saveABoolean (String key, boolean value) {
        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getABoolean (String key) {
        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        boolean returnInt = mSharedPref.getBoolean(key, false);
        return returnInt;
    }

    public static void saveAnInteger (String key, int value) {
        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getAnInteger (String key) {
        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        int returnInt = mSharedPref.getInt(key, 0);
        return returnInt;
    }

    public static void saveAllPrefs() {
        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt("points", points);
        editor.putInt("life", life);
        editor.putInt("energy", energy);
        editor.putInt("trouser", trouserToWear);
        editor.apply();
    }

    public static void getAllPrefs () {
        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        points = mSharedPref.getInt("points", 0);
        life = mSharedPref.getInt("life", 100);
        energy = mSharedPref.getInt("life", 100);
        trouserToWear = mSharedPref.getInt("trouser", R.mipmap.pants00);
    }


    public static <E> ArrayAdapter<E> setNewAdapter (E classToObject, ArrayAdapter adapter) {

        ArrayList<E> listItems = new ArrayList<E>();
        for (int i = 0; i < 7; i++ ) {
            if ( == Items)
            listItems.add(classToObject.innitItem(i));

        }

        ArrayAdapter<E> listAdapter = new ArrayAdapter<E>(this,listItems);
        return listAdapter;
    }
}
