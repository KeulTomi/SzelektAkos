package com.example.android.szelektakos.games;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.szelektakos.R;
import com.example.android.szelektakos.SzelektAkos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.android.szelektakos.games.TrashesFactory.innitTrashes;
import static com.example.android.szelektakos.games.TrashesFactory.mCorrectPicture;
import static com.example.android.szelektakos.games.TrashesFactory.mCurrentGarbage;

public class TrashesGame extends AppCompatActivity implements View.OnClickListener {
    public static ImageView firstTrash;
    public static ImageView secondTrash;
    public static ImageView thirdTrash;
    public static ImageView fourthTrash;
    public static ImageView fifthTrash;
    public static ImageView sixthTrash;
    public ImageView closeTg;
    public ImageView garbage;
    private TextView reachedPointsTxt;
    private int reachedPointsTG;
    private float garbageX;
    private float garbageY;
    private Vibrator mVibrator;
    public static ImageView[] trashList;
    public static Integer[] mipmapList = {R.mipmap.trash_blue, R.mipmap.trash_braun, R.mipmap.trash_build, R.mipmap.trash_green,
            R.mipmap.trash_white, R.mipmap.trash_yellow};
    public static Integer[] placesOfTraahesList = {0, 1, 2, 3, 4, 5};
    public static List<Integer> dinamicMipMapList;
    public static ArrayList<Integer> dinamicTrashPlaceList = new ArrayList<>();
    public final static int MSG_GAME_TIME_START = 4;
    public final static int MSG_GAME_TIME_NULL = 5;
    public final static int MSG_REPLACE = 6;
    final int GAME_TIME_REFRESHED_TIME = 100;
    public static Handler uiHandlerTG;
    private Future gameTimeStopper;
    private ProgressBar gameTimeProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trashes_game);

        firstTrash = (ImageView) findViewById(R.id.first_trash);
        secondTrash = (ImageView) findViewById(R.id.second_trash);
        thirdTrash = (ImageView) findViewById(R.id.third_trash);
        fourthTrash = (ImageView) findViewById(R.id.fourth_trash);
        fifthTrash = (ImageView) findViewById(R.id.fifth_trash);
        sixthTrash = (ImageView) findViewById(R.id.sixth_trash);

        closeTg = (ImageView) findViewById(R.id.close_TG);
        garbage = (ImageView) findViewById(R.id.garbage);
        reachedPointsTxt = (TextView) findViewById(R.id.reached_points_txt_TG);
        gameTimeProgress = (ProgressBar) findViewById(R.id.game_time_progress_TG);

        firstTrash.setOnClickListener(this);
        secondTrash.setOnClickListener(this);
        thirdTrash.setOnClickListener(this);
        fourthTrash.setOnClickListener(this);
        fifthTrash.setOnClickListener(this);
        sixthTrash.setOnClickListener(this);
        closeTg.setOnClickListener(this);

        //A telefon rezgetéséhez szükséges vibrátor
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Felépítjük a kukák helyét és hozzátartozó képeket plussz az innitTrashes()-ben kiszámítjuk a jó választ
        refreshTrashPlaces(0,0);

        // Activity üzenetkezelője
        uiHandlerTG = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_GAME_TIME_START:
                        gameTimeProgress.setProgress((Integer) msg.obj);
                        break;

                    case MSG_GAME_TIME_NULL:
                        gameTimeProgress.setProgress((Integer) msg.obj);
                        break;

                    case MSG_REPLACE:
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) garbage.getLayoutParams();
                        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                        garbage.setLayoutParams(lp);

                    default:
                        // Ha valamilyen más üzenet érkezik, itt lehet lekezelni
                        break;
                }
            }
        };

        //Nullázzuk a gameTime-ot
        SzelektAkos.setGameTimeNull(MSG_GAME_TIME_NULL);

        // A játékidő időzítő futtatható kódja, ez fut le minden időzítés után

        final Runnable gameTimer = new Runnable() {
            @Override
            public void run() {
                if(!((Activity) TrashesGame.this).isFinishing())
                {
                    // Saját magát hívja késleltetés után
                    SzelektAkos.increaseGameTime(MSG_GAME_TIME_START);
                    if (SzelektAkos.gameTime <= 500) {
                        uiHandlerTG.postDelayed(this, GAME_TIME_REFRESHED_TIME);
                    }
                    else {
                        //show dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrashesGame.this);
                        builder.setMessage("Gratulálunk!" + "\n" + String.valueOf(reachedPointsTG) + " pontot szereztél")
                                .setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Kilép az activity-ből
                                        SzelektAkos.increasePoints(reachedPointsTG);
                                        gameTimeStopper.cancel(true);
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();

                        alert.show();
                    }
                }
            }
        };
        //Képessé tesszük a runnable-t stoppolásra
        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        gameTimeStopper = threadPoolExecutor.submit(gameTimer);

        // Időzítők beindítása (első futtatás)
        uiHandlerTG.postDelayed(gameTimer, GAME_TIME_REFRESHED_TIME);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.first_trash:
                float correctTrashX = firstTrash.getX();
                float correctTrashY = firstTrash.getY();

                    if (dinamicTrashPlaceList.get(0) == mCorrectPicture) {
                        reachedPointsTG += 1;
                        reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                        dinamicTrashPlaceList.clear();


                        //Animáció indítása
                        garbageAnimator(correctTrashX,correctTrashY);
                    } else {
                        mVibrator.vibrate(300);
                        dinamicTrashPlaceList.clear();
                        refreshTrashPlaces(correctTrashX, correctTrashY);
                    }
                break;

            case R.id.second_trash:
                float correctTrash2X = secondTrash.getX();
                float correctTrash2Y = secondTrash.getY();

                if (dinamicTrashPlaceList.get(1) == mCorrectPicture) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();
                    dinamicTrashPlaceList.clear();

                    //Animáció indítása
                    garbageAnimator(correctTrash2X,correctTrash2Y);
                }
                else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces(correctTrash2X, correctTrash2Y);
                }
                break;

            case R.id.third_trash:
                float correctTrash3X = secondTrash.getX();
                float correctTrash3Y = secondTrash.getY();

                if (dinamicTrashPlaceList.get(2) == mCorrectPicture) {
                    //Plussz pont hozzáadása az elért pontokhoz
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));

                    //Animáció indítása
                    garbageAnimator(correctTrash3X,correctTrash3Y);
                }
                else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces(correctTrash3X, correctTrash3Y);
                }
                break;

            case R.id.fourth_trash:
                float correctTrash4X = fourthTrash.getX();
                float correctTrash4Y = fourthTrash.getY();

                if (dinamicTrashPlaceList.get(3) == mCorrectPicture) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();

                    //Animáció indítása
                    garbageAnimator(correctTrash4X,correctTrash4Y);
                }
                else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces(correctTrash4X, correctTrash4Y);
                }
                break;

            case R.id.fifth_trash:
                float correctTrash5X = fifthTrash.getX();
                float correctTrash5Y = fifthTrash.getY();

                if (dinamicTrashPlaceList.get(4) == mCorrectPicture) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();

                    //Animáció indítása
                    garbageAnimator(correctTrash5X,correctTrash5Y);
                }
                else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces(correctTrash5X, correctTrash5Y);
                }
                break;

            case R.id.sixth_trash:
                float correctTrash6X = sixthTrash.getX();
                float correctTrash6Y = sixthTrash.getY();

                if (dinamicTrashPlaceList.get(5) == mCorrectPicture) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();

                    //Animáció indítása
                    garbageAnimator(correctTrash6X,correctTrash6Y);
                }
                else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces(correctTrash6X, correctTrash6Y);
                }
                break;

            case R.id.close_TG:
                SzelektAkos.increasePoints(reachedPointsTG);
                gameTimeStopper.cancel(true);
                finish();
        }
    }

    public void refreshTrashPlaces (float replaceX, float replaceY) {
        Random r = new Random();
        trashList = new ImageView[]{firstTrash, secondTrash, thirdTrash, fourthTrash, fifthTrash, sixthTrash};
        dinamicMipMapList = new ArrayList<Integer>(Arrays.asList(mipmapList));
        int index = 0;
        int placeNumber;
        ImageView place;

        while (index < 6){
            placeNumber = r.nextInt(dinamicMipMapList.size());
            place = trashList[index];
            place.setImageResource(dinamicMipMapList.get(placeNumber));
            dinamicTrashPlaceList.add(dinamicMipMapList.get(placeNumber));
            dinamicMipMapList.remove(placeNumber);
            index++;
        }
        //Újra inicializáljuk a kukákat és a szemetet középen
        innitTrashes();
        //Beallítjuk a követező szemetet és az ImageView-t visszarakjuk középre
        garbage.setImageResource(mCurrentGarbage);
        final Runnable replacer = new Runnable() {
            @Override
            public void run() {
                if(!((Activity) TrashesGame.this).isFinishing())
                {
                    SzelektAkos.updateGameTime(MSG_REPLACE);
                }
            }
        };
        if (replaceX!=0) uiHandlerTG.postDelayed(replacer, 1);
//        if (replaceX != 0 || replaceY !=0) {
//            ObjectAnimator animationX = ObjectAnimator.ofFloat(garbage, "x", 200);
//            ObjectAnimator animationY = ObjectAnimator.ofFloat(garbage, "y", 200);
//            animationX.setDuration(1000);
//            animationY.setDuration(1000);
//            animationX.setRepeatMode(ValueAnimator.REVERSE);
//            animationY.setRepeatMode(ValueAnimator.REVERSE);
//            animationX.start();
//            animationY.start();
//        }
    }

    public void garbageAnimator(final float correctTrashX, final float correctTrashY) {
        //Animáció a szemétre
        ObjectAnimator animationX = ObjectAnimator.ofFloat(garbage, "x", correctTrashX);
        ObjectAnimator animationY = ObjectAnimator.ofFloat(garbage, "y", correctTrashY);
        animationX.setDuration(600);
        animationY.setDuration(600);
        animationX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Újra felállítása a layoutnak
                refreshTrashPlaces(correctTrashX , correctTrashY );
            }
        });
        animationX.setRepeatMode(ValueAnimator.REVERSE);
        animationY.setRepeatMode(ValueAnimator.REVERSE);
        animationX.start();
        animationY.start();

    }

}
