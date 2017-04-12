package com.example.android.szelektakos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    private static Handler progressBarHandler = new Handler();
    public static int gameTime;



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

    public static void changeEnergy (int deltaValue) {

        if ( deltaValue <= 0) {
            energy = (energy + deltaValue <= 0 ? 0 : energy + deltaValue);
        }
        else {
            energy = (energy + deltaValue <= 100 ? energy+deltaValue: 100);
        }
        updateProgressBar(MainActivity.MSG_UPDATE_ENERGY);
    }

    public static void changeLifeValue(int deltaValue) {

        if (deltaValue <= 0) {
            life = (life + deltaValue >= 0 ? life + deltaValue : 0);
        }
        else {
            life = (life + deltaValue >= 100 ? 100 : life+deltaValue);
        }

        updateProgressBar(MainActivity.MSG_UPDATE_LIFE);
    }

    private static void updateProgressBar(int msgCode) {

        // Progressbar frissítéséhez üzenet küldése a MainActivity üzenetkezelőjének
        Message msgToMainActivity = new Message();

        switch (msgCode) {
            case MainActivity.MSG_UPDATE_LIFE:
                // Üzenet előkészítése életerő frissítéséhez
                msgToMainActivity.what = MainActivity.MSG_UPDATE_LIFE; // Üzenetkód beállítása
                msgToMainActivity.obj = (Integer) life; // Küldendő érték beállítása
                break;
            case MainActivity.MSG_UPDATE_ENERGY:
                // Üzenet előkészítése energia frissítéséhez
                msgToMainActivity.what = MainActivity.MSG_UPDATE_LIFE; // Üzenetkód beállítása
                msgToMainActivity.obj = (Integer) energy; // Küldendő érték beállítása
                break;
        }

        MainActivity.uiHandler.sendMessage(msgToMainActivity); // Üzenet küldése
    }

    private static void updateGameTime(int msg) {
        Message msgToPickOneGame = new Message();

        switch (msg) {
            case PickOneGame.MSG_GAME_TIME_START:

                msgToPickOneGame.what = PickOneGame.MSG_GAME_TIME_START; //Üzenetkód beállítása
                msgToPickOneGame.obj = (Integer) gameTime;
                PickOneGame.uiHandlerPOG.sendMessage(msgToPickOneGame);
                break;

            case PickOneGame.MSG_GAME_TIME_NULL:

                msgToPickOneGame.what = PickOneGame.MSG_GAME_TIME_NULL;
                msgToPickOneGame.obj = (Integer) gameTime;
                PickOneGame.uiHandlerPOG.sendMessage(msgToPickOneGame);
                break;
        }
    }

    public static void increaseGameTime () {
        gameTime++;
        updateGameTime(PickOneGame.MSG_GAME_TIME_START);
    }

    public static void setGameTimeNull () {
        gameTime = 0;
        updateGameTime(PickOneGame.MSG_GAME_TIME_NULL);
    }

    public static int getLife() {
        return life;
    }

    public static int getEnergy() {
        return energy;
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


//    public static <E> ArrayAdapter<E> setNewAdapter (E classToObject, ArrayAdapter adapter) {
//
//        ArrayList<E> listItems = new ArrayList<E>();
//        for (int i = 0; i < 7; i++ ) {
//            if ( == Items)
//            listItems.add(classToObject.innitItem(i));
//
//        }
//
//        ArrayAdapter<E> listAdapter = new ArrayAdapter<E>(this,listItems);
//        return listAdapter;
//    }
}
