package com.example.android.szelektakos.Games;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.szelektakos.R;
import com.example.android.szelektakos.SzelektAkos;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TrueFalseGame extends AppCompatActivity implements View.OnClickListener {

    private static LinearLayout mTrue;
    private static LinearLayout mFalse;
    private Vibrator mVibrator;
    public static Handler uiHandlerTF;
    final static int MSG_GAME_TIME_START = 2;
    final static int MSG_GAME_TIME_NULL = 3;
    private static ProgressBar gameTimeProgressTF;
    private final int GAME_TIME_REFRESHED_TIME = 100;
    private int reachedPointsTF;
    private static TextView reachedPointsTxtTF;
    private static TextView questionTxtTF;
    private static ImageView closeTFG;
    private static Future<?> gameTimeStopper;
    private Runnable gameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false_game);

        //Inicializálások
        mTrue = (LinearLayout) findViewById(R.id.layout_true);
        mFalse = (LinearLayout) findViewById(R.id.layout_false);
        gameTimeProgressTF = (ProgressBar) findViewById(R.id.game_time_progress_TFG);
        reachedPointsTxtTF = (TextView) findViewById(R.id.reached_points_txt_TFG);
        questionTxtTF = (TextView) findViewById(R.id.question_txt_TFG);
        closeTFG = (ImageView) findViewById(R.id.close_TFG);

        //OnClickListener a két igaz - hamis layout-ra
        mTrue.setOnClickListener(this);
        mFalse.setOnClickListener(this);
        closeTFG.setOnClickListener(this);

        //Megkapjuk a kérdést és kiíratjuk
        getTheCurrentQuestion();

        //A telefon rezgetéséhez szükséges vibrátor
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Activity üzenetkezelője
        uiHandlerTF = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_GAME_TIME_START:
                        gameTimeProgressTF.setProgress((Integer) msg.obj);
                        break;

                    case MSG_GAME_TIME_NULL:
                        gameTimeProgressTF.setProgress((Integer) msg.obj);
                        break;

                    default:
                        // Ha valamilyen más üzenet érkezik, itt lehet lekezelni
                        break;
                }
            }
        };

        //Nullázzuk a gameTime-ot
        SzelektAkos.setGameTimeNull(MSG_GAME_TIME_NULL);

        // A játékidő időzítő futtatható kódja, ez fut le minden időzítés után7


        gameTimer = new Runnable() {
            @Override
            public void run() {

                if(!((Activity) TrueFalseGame.this).isFinishing())
                {
                // Saját magát hívja késleltetés után
                SzelektAkos.increaseGameTime(MSG_GAME_TIME_START);

                if (SzelektAkos.gameTime <= 500) {
                    uiHandlerTF.postDelayed(this, GAME_TIME_REFRESHED_TIME);
                }
                else {
                        //show dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrueFalseGame.this);
                        builder.setMessage("Gratulálunk!" + "\n" + String.valueOf(reachedPointsTF) + " pontot szereztél")
                                .setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Kilép az activity-ből
                                        SzelektAkos.increasePoints(reachedPointsTF);
                                    gameTimeStopper.cancel(true);
                                        uiHandlerTF.removeCallbacksAndMessages(gameTimer);
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
        uiHandlerTF.postDelayed(gameTimer, GAME_TIME_REFRESHED_TIME);
    }

    public void getTheCurrentQuestion() {
        TFQuestionFactory.questionFactoryTF();
        questionTxtTF.setText(TFQuestionFactory.getmQuestionTf());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.layout_false:
                    if (TFQuestionFactory.getmCorrectAnswer() == false) {
                        reachedPointsTF += 1;
                        reachedPointsTxtTF.setText(String.valueOf(reachedPointsTF));
                        getTheCurrentQuestion();
                    } else {
                        mVibrator.vibrate(300);
                        getTheCurrentQuestion();
                    }
                break;

            case R.id.layout_true:
                    if (TFQuestionFactory.getmCorrectAnswer() == true) {
                        reachedPointsTF += 1;
                        reachedPointsTxtTF.setText(String.valueOf(reachedPointsTF));
                        getTheCurrentQuestion();
                    } else {
                        mVibrator.vibrate(300);
                        getTheCurrentQuestion();
                    }
                break;

            case R.id.close_TFG:
                    SzelektAkos.increasePoints(reachedPointsTF);
                    gameTimeStopper.cancel(true);
                    uiHandlerTF.removeCallbacksAndMessages(gameTimer);
                    finish();
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        uiHandlerTF.removeCallbacksAndMessages(gameTimer);
        super.onDestroy();
    }
}
