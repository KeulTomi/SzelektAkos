package hu.foxplan.keult.szelektakos.games;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.ScaleHelper;
import hu.foxplan.keult.szelektakos.SzelektAkos;
import hu.foxplan.keult.szelektakos.mainscreen.MainActivity;

public class TrashesGame extends AppCompatActivity implements View.OnClickListener {
    public final static int MSG_GAME_TIME_START = 4;
    public final static int MSG_GAME_TIME_NULL = 5;
    public final static int MSG_PROOGRESS_LIFE = 11;
    public final static int MSG_PROOGRESS_ENERGY = 12;
    public final static int MSG_REPLACE = 6;
    public static ImageView firstTrash;
    public static ImageView secondTrash;
    public static ImageView thirdTrash;
    public static ImageView fourthTrash;
    public static ImageView fifthTrash;
    public static ImageView sixthTrash;
    public static ImageView[] trashList;
    public static Integer[] drawableList = {hu.foxplan.keult.szelektakos.R.drawable.trash_blue, hu.foxplan.keult.szelektakos.R.drawable.trash_braun, hu.foxplan.keult.szelektakos.R.drawable.trash_build, hu.foxplan.keult.szelektakos.R.drawable.trash_green,
            hu.foxplan.keult.szelektakos.R.drawable.trash_white, hu.foxplan.keult.szelektakos.R.drawable.trash_yellow};
    public static Integer[] placesOfTraahesList = {0, 1, 2, 3, 4, 5};
    public static List<Integer> dinamicMipMapList;
    public static ArrayList<Integer> dinamicTrashPlaceList = new ArrayList<>();
    public static Handler uiHandlerTG;
    final int GAME_TIME_REFRESHED_TIME = 100;
    public ImageView closeTg;
    public ImageView garbage;
    private TextView reachedPointsTxt;
    private int reachedPointsTG;
    private int firstWindow = 0;
    private float garbageX;
    private float garbageY;
    private Vibrator mVibrator;
    private Future gameTimeStopper;
    private ProgressBar gameTimeProgress;
    private ProgressBar lifeProgress;
    private ProgressBar energyProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hu.foxplan.keult.szelektakos.R.layout.activity_trashes_game);

        firstTrash = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.first_trash);
        secondTrash = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.second_trash);
        thirdTrash = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.third_trash);
        fourthTrash = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.fourth_trash);
        fifthTrash = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.fifth_trash);
        sixthTrash = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.sixth_trash);

        closeTg = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.close_TG);
        garbage = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.garbage);
        reachedPointsTxt = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.reached_points_txt_TG);
        gameTimeProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.game_time_progress_TG);
        lifeProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.progress_life_TG);
        energyProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.progress_energy_TG);

        firstTrash.setOnClickListener(this);
        secondTrash.setOnClickListener(this);
        thirdTrash.setOnClickListener(this);
        fourthTrash.setOnClickListener(this);
        fifthTrash.setOnClickListener(this);
        sixthTrash.setOnClickListener(this);
        closeTg.setOnClickListener(this);

        //A két progressbarból levonunk 5-öt
        lifeProgress.setProgress(MainActivity.life.getProgress() - 5);

        energyProgress.setProgress(MainActivity.energy.getProgress() - 5);

        //A telefon rezgetéséhez szükséges vibrátor
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Felépítjük a kukák helyét és hozzátartozó képeket plussz az innitTrashes()-ben kiszámítjuk a jó választ
        refreshTrashPlaces();

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

                    case MSG_PROOGRESS_LIFE:
                        lifeProgress.setProgress((Integer) msg.obj);
                        break;

                    case MSG_PROOGRESS_ENERGY:
                        energyProgress.setProgress((Integer) msg.obj);
                        break;

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
                if (!TrashesGame.this.isFinishing()) {
                    // Saját magát hívja késleltetés után
                    SzelektAkos.increaseGameTime(MSG_GAME_TIME_START);
                    if (SzelektAkos.gameTime <= 813) {
                        uiHandlerTG.postDelayed(this, GAME_TIME_REFRESHED_TIME);
                    } else {
                        //show dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrashesGame.this);
                        builder
                                .setCancelable(false)
                                .setMessage("Gratulálunk!" + "\n" + String.valueOf(reachedPointsTG) + " pontot szereztél")
                                .setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Kilép az activity-ből
                                        SzelektAkos.increasePoints(reachedPointsTG);
                                        SzelektAkos.comeBackFromGame = true;
                                        gameTimeStopper.cancel(true);
                                        finish();
                                        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_down);
                                    }
                                });
                        AlertDialog alert = builder.create();

                        alert.show();
                    }
                }
            }
        };

        final Runnable lifeProgresser = new Runnable() {
            @Override
            public void run() {
                SzelektAkos.decreaseInGameProgress(MSG_PROOGRESS_LIFE, "life");
            }
        };

        final Runnable energyProgresser = new Runnable() {
            @Override
            public void run() {
                SzelektAkos.decreaseInGameProgress(MSG_PROOGRESS_ENERGY, "energy");
            }
        };

        //Képessé tesszük a runnable-t stoppolásra
        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        gameTimeStopper = threadPoolExecutor.submit(gameTimer);

        // Időzítők beindítása (első futtatás)
        uiHandlerTG.post(lifeProgresser);
        uiHandlerTG.post(energyProgresser);
        uiHandlerTG.postDelayed(gameTimer, GAME_TIME_REFRESHED_TIME);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case hu.foxplan.keult.szelektakos.R.id.first_trash:

                if ((dinamicTrashPlaceList.get(0) == TrashesFactory.mCorrectPicture) ||
                        (dinamicTrashPlaceList.get(0) == TrashesFactory.mCorrectPicture2)) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();

                    float correctTrashX = firstTrash.getX();
                    float correctTrashY = firstTrash.getY();

                    //Animáció indítása
                    garbageAnimator(correctTrashX, correctTrashY);
                } else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces();
                }
                break;

            case hu.foxplan.keult.szelektakos.R.id.second_trash:

                if ((dinamicTrashPlaceList.get(1) == TrashesFactory.mCorrectPicture) ||
                        (dinamicTrashPlaceList.get(1) == TrashesFactory.mCorrectPicture2)) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();
                    float correctTrashX = secondTrash.getX();
                    float correctTrashY = secondTrash.getY();

                    //Animáció indítása
                    garbageAnimator(correctTrashX, correctTrashY);
                } else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces();
                }
                break;

            case hu.foxplan.keult.szelektakos.R.id.third_trash:

                if ((dinamicTrashPlaceList.get(2) == TrashesFactory.mCorrectPicture) ||
                        (dinamicTrashPlaceList.get(2) == TrashesFactory.mCorrectPicture2)) {
                    //Plussz pont hozzáadása az elért pontokhoz
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();
                    float correctTrashX = thirdTrash.getX();
                    float correctTrashY = thirdTrash.getY();

                    //Animáció indítása
                    garbageAnimator(correctTrashX, correctTrashY);
                } else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces();
                }
                break;

            case hu.foxplan.keult.szelektakos.R.id.fourth_trash:

                if ((dinamicTrashPlaceList.get(3) == TrashesFactory.mCorrectPicture) ||
                        (dinamicTrashPlaceList.get(3) == TrashesFactory.mCorrectPicture2)) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();
                    float correctTrashX = fourthTrash.getX();
                    float correctTrashY = fourthTrash.getY();


                    //Animáció indítása
                    garbageAnimator(correctTrashX, correctTrashY);
                } else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces();
                }
                break;

            case hu.foxplan.keult.szelektakos.R.id.fifth_trash:

                if ((dinamicTrashPlaceList.get(4) == TrashesFactory.mCorrectPicture) ||
                        (dinamicTrashPlaceList.get(4) == TrashesFactory.mCorrectPicture2)) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();
                    float correctTrashX = fifthTrash.getX();
                    float correctTrashY = fifthTrash.getY();

                    //Animáció indítása
                    garbageAnimator(correctTrashX, correctTrashY);
                } else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces();
                }
                break;

            case hu.foxplan.keult.szelektakos.R.id.sixth_trash:

                if ((dinamicTrashPlaceList.get(5) == TrashesFactory.mCorrectPicture) ||
                        (dinamicTrashPlaceList.get(5) == TrashesFactory.mCorrectPicture2)) {
                    reachedPointsTG += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPointsTG));
                    dinamicTrashPlaceList.clear();
                    float correctTrashX = sixthTrash.getX();
                    float correctTrashY = sixthTrash.getY();

                    //Animáció indítása
                    garbageAnimator(correctTrashX, correctTrashY);
                } else {
                    mVibrator.vibrate(300);
                    dinamicTrashPlaceList.clear();
                    refreshTrashPlaces();
                }
                break;

            case hu.foxplan.keult.szelektakos.R.id.close_TG:
                SzelektAkos.increasePoints(reachedPointsTG);
                SzelektAkos.comeBackFromGame = true;
                gameTimeStopper.cancel(true);
                dinamicTrashPlaceList.clear();
                finish();
                overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_down);
                break;
        }
    }

    public void refreshTrashPlaces() {
        Random r = new Random();
        trashList = new ImageView[]{firstTrash, secondTrash, thirdTrash, fourthTrash, fifthTrash, sixthTrash};
        dinamicMipMapList = new ArrayList<Integer>(Arrays.asList(drawableList));
        int index = 0;
        int placeNumber;
        ImageView place;

        while (index < 6) {
            placeNumber = r.nextInt(dinamicMipMapList.size());
            place = trashList[index];
            place.setImageResource(dinamicMipMapList.get(placeNumber));
            dinamicTrashPlaceList.add(dinamicMipMapList.get(placeNumber));
            dinamicMipMapList.remove(placeNumber);
            index++;
        }
        //Újra inicializáljuk a kukákat és a szemetet középen
        TrashesFactory.innitTrashes();
        //Beallítjuk a követező szemetet és az ImageView-t visszarakjuk középre
        garbage.setImageResource(TrashesFactory.mCurrentGarbage);

        if (reachedPointsTG > 0) {
            ViewGroup.LayoutParams layoutparams = garbage.getLayoutParams();
            Point pos = (Point) garbage.getTag();
            garbage.setX(garbageX);
            garbage.setY(garbageY);
        }

    }

    public void garbageAnimator(final float correctTrashX, final float correctTrashY) {

        // Pozíció mentése, hogy az animálás után vissza lehessen állítani középre
        garbageX = garbage.getX();
        garbageY = garbage.getY();

        //Animáció a szemétre
        ObjectAnimator animationX = ObjectAnimator.ofFloat(garbage, "x", correctTrashX);
        ObjectAnimator animationY = ObjectAnimator.ofFloat(garbage, "y", correctTrashY);
        animationX.setDuration(600);
        animationY.setDuration(600);
        animationX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Újra felállítása a layoutnak
                refreshTrashPlaces();
            }
        });
        animationX.setRepeatMode(ValueAnimator.REVERSE);
        animationY.setRepeatMode(ValueAnimator.REVERSE);
        animationX.start();
        animationY.start();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ScaleHelper.scaleContents(
                findViewById(R.id.trash_game_root),
                findViewById(R.id.trash_game_container),
                false);

        if (firstWindow == 0) {
            if (energyProgress.getProgress() <= 0) {
//            if (!PickOneGame.this.isFinishing()) {
//            }
//            else {
                //show dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TrashesGame.this);
                builder.setMessage("Szelekt Ákos elfáradt aludnia kell!")
                        .setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();

                alert.show();
//            }
            }
        }
        firstWindow++;
    }

}
