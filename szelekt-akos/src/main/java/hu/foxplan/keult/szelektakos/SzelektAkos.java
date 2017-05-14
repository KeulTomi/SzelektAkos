package hu.foxplan.keult.szelektakos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hu.foxplan.keult.szelektakos.games.PickOneGame;
import hu.foxplan.keult.szelektakos.games.TrashesGame;
import hu.foxplan.keult.szelektakos.games.TrueFalseGame;
import hu.foxplan.keult.szelektakos.games.WordPuzzle;
import hu.foxplan.keult.szelektakos.mainscreen.MainActivity;

/**
 * Created by Tomi on 2017. 03. 28..
 */

public class SzelektAkos extends Application {

    public static int gameTime;
    public static int inGameProgress;
    public static boolean comeBackFromGame = false;
    public static boolean installStatus;
    public static float displayDensity;
    public static int displayHeight;
    public static int displayWidth;
    public static int energy;
    public static int life;
    public static boolean[] mainBoughtTrousersList = new boolean[6];
    public static int savedVersion;
    public static SharedPreferences mSharedPref;
    private static int points;
    private static int trouserToWear;
    private static List<Integer> avaiableTrousers = new ArrayList<>();
    private static Context appContext;
    private static Handler progressBarHandler = new Handler();

    public static void innitApp(Context context) {
        appContext = context;
        displayDensity = context.getResources().getDisplayMetrics().density;
        displayHeight = context.getResources().getDisplayMetrics().heightPixels;
        displayWidth = context.getResources().getDisplayMetrics().widthPixels;
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
            energy = (energy - deltaValue <= 0 ? 0 : energy + deltaValue);
        }
        else {
            energy = (energy + deltaValue <= 100 ? energy+deltaValue: 100);
        }
        updateProgressBar(MainActivity.MSG_UPDATE_ENERGY);
    }

    public static void changeLifeValue(int deltaValue) {

        if (deltaValue <= 0) {
            life = (life - deltaValue >= 0 ? life + deltaValue : 0);
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
                msgToMainActivity.obj = life; // Küldendő érték beállítása
                break;
            case MainActivity.MSG_UPDATE_ENERGY:
                // Üzenet előkészítése energia frissítéséhez
                msgToMainActivity.what = MainActivity.MSG_UPDATE_ENERGY; // Üzenetkód beállítása
                msgToMainActivity.obj = energy; // Küldendő érték beállítása
                break;
        }

        MainActivity.uiHandler.sendMessage(msgToMainActivity); // Üzenet küldése
    }

    public static void updateProgresbarsInGame (int msg){
        Message msgToAGame = new Message();

        switch (msg) {
            case PickOneGame.MSG_PROOGRESS_LIFE:

                msgToAGame.what = PickOneGame.MSG_PROOGRESS_LIFE; //Üzenetkód beállítása
                msgToAGame.obj = inGameProgress;
                PickOneGame.uiHandlerPOG.sendMessage(msgToAGame);
                break;

            case PickOneGame.MSG_PROOGRESS_ENERGY:

                msgToAGame.what = PickOneGame.MSG_PROOGRESS_ENERGY; //Üzenetkód beállítása
                msgToAGame.obj = inGameProgress;
                PickOneGame.uiHandlerPOG.sendMessage(msgToAGame);
                break;
        }
    }

    public static void updateGameTime(int msg) {
        Message msgToAGame = new Message();

        switch (msg) {
            case PickOneGame.MSG_GAME_TIME_NULL:

                msgToAGame.what = PickOneGame.MSG_GAME_TIME_NULL; //Üzenetkód beállítása
                msgToAGame.obj = gameTime;
                PickOneGame.uiHandlerPOG.sendMessage(msgToAGame);
                break;

            case PickOneGame.MSG_GAME_TIME_START:

                msgToAGame.what = PickOneGame.MSG_GAME_TIME_START;
                msgToAGame.obj = gameTime;
                PickOneGame.uiHandlerPOG.sendMessage(msgToAGame);
                break;

            case TrueFalseGame.MSG_GAME_TIME_NULL:
                msgToAGame.what = TrueFalseGame.MSG_GAME_TIME_NULL;
                msgToAGame.obj = gameTime;
                TrueFalseGame.uiHandlerTF.sendMessage(msgToAGame);
                break;

            case TrueFalseGame.MSG_GAME_TIME_START:
                msgToAGame.what = TrueFalseGame.MSG_GAME_TIME_START;
                msgToAGame.obj = gameTime;
                TrueFalseGame.uiHandlerTF.sendMessage(msgToAGame);
                break;

            case TrashesGame.MSG_GAME_TIME_NULL:
                msgToAGame.what = TrashesGame.MSG_GAME_TIME_START;
                msgToAGame.obj = gameTime;
                TrashesGame.uiHandlerTG.sendMessage(msgToAGame);
                break;

            case TrashesGame.MSG_GAME_TIME_START:
                msgToAGame.what = TrashesGame.MSG_GAME_TIME_START;
                msgToAGame.obj = gameTime;
                TrashesGame.uiHandlerTG.sendMessage(msgToAGame);
                break;

            case WordPuzzle.MSG_GAME_TIME_START:
                msgToAGame.what = WordPuzzle.MSG_GAME_TIME_START;
                msgToAGame.obj = gameTime;
                WordPuzzle.uiHandlerWP.sendMessage(msgToAGame);
                break;

            case WordPuzzle.MSG_GAME_TIME_NULL:
                msgToAGame.what = WordPuzzle.MSG_GAME_TIME_NULL;
                msgToAGame.obj = gameTime;
                WordPuzzle.uiHandlerWP.sendMessage(msgToAGame);
                break;

        }
    }

    public static void increaseGameTime (int msg) {
        gameTime++;
        updateGameTime(msg);
    }

    public static void setGameTimeNull (int msg) {
        gameTime = 0;
        updateGameTime(msg);
    }

    public static void decreaseInGameProgress (int msg, String progressName) {
        switch (progressName){
            case "energy":
                inGameProgress = MainActivity.energy.getProgress() - 5;
                updateProgresbarsInGame(msg);
                break;
            case "life":
                inGameProgress = MainActivity.life.getProgress() - 5;
                updateProgresbarsInGame(msg);
                break;
        }

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
        editor.putString(key, "hord");
        editor.apply();
    }

    public static String getABoolean (String key) {
        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        String returnStr = mSharedPref.getString(key, null);
        return returnStr;
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
        editor.putBoolean("installStatus", false);
        editor.apply();
    }

    public static void getAllPrefs () {
        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        points = 1500; //mSharedPref.getInt("points", 0);
        life = mSharedPref.getInt("life", 100);
        energy = mSharedPref.getInt("energy", 100);
        trouserToWear = mSharedPref.getInt("trouser", R.drawable.pants00);
        installStatus = mSharedPref.getBoolean("installStatus", true);
    }

    public static void clearAllPrefs() {

        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.clear();
        editor.apply();

    }

    public static void saveTroousersToMainData (boolean[] classBoughtTrousersList){
        mainBoughtTrousersList = classBoughtTrousersList;
    }


    public static void scaleImageView(ImageView view, Rect imageRect, float scaleX, float scaleY) {

        // ImageView méretezése
        view.getLayoutParams().width = (int) (imageRect.right * scaleX);

        view.getLayoutParams().height = (int) (imageRect.bottom * scaleY);

        view.setScaleType(ImageView.ScaleType.FIT_XY);

    }

    public static void positionImageView(ImageView view, Rect imageRect, float scaleX, float scaleY) {

        view.setX(imageRect.left * scaleX);
        view.setY(imageRect.top * (scaleY == 0 ? scaleX : scaleY));


    }

    public static int getVersionCode() {

        int versionCode;

        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        versionCode = mSharedPref.getInt("versionCode", 0);

        return versionCode;
    }

    public static void saveVersionCode(int versionCode) {

        mSharedPref = appContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt("versionCode", versionCode);
        editor.apply();

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
