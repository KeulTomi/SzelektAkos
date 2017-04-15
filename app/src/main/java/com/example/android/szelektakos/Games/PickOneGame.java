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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.szelektakos.MainScreen.MainActivity;
import com.example.android.szelektakos.R;
import com.example.android.szelektakos.SzelektAkos;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PickOneGame extends AppCompatActivity implements View.OnTouchListener {

    private TextView questionTxt;
    private TextView firstAnswerTxt;
    private TextView secondAnswerTxt;
    private TextView thirdAnswerTxt;
    private TextView reachedPointsTxt;
    private ProgressBar gameTimeProgress;
    private ProgressBar lifeProgress;
    private ProgressBar energyProgress;
    //TODO A gombnyomásra ki kell cserélni a képeket.
    public ImageView firstAnswerImg;
    public ImageView secondAnswerImg;
    private ImageView thirdAnswerImg;
    private ImageView closeTheGame;
    private int reachedPointsPOG;
    private Vibrator mVibrator;
    final static int MSG_GAME_TIME_START = 0;
    final static int MSG_GAME_TIME_NULL = 1;
    final int GAME_TIME_REFRESHED_TIME = 100;
    public static Handler uiHandlerPOG;
    private Future gameTimeStopper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_one_game);

        //Kerdéshez szükséges Textek és képek innit
        questionTxt = (TextView) findViewById(R.id.question_txt_POG);
        firstAnswerTxt = (TextView) findViewById(R.id.first_answer_txt);
        secondAnswerTxt = (TextView) findViewById(R.id.second_answer_txt);
        thirdAnswerTxt = (TextView) findViewById(R.id.third_answer_txt);
        reachedPointsTxt = (TextView) findViewById(R.id.reached_points_txt_POG);
        gameTimeProgress = (ProgressBar) findViewById(R.id.game_time_progress_POG);
        lifeProgress = (ProgressBar) findViewById(R.id.progress_life_POG);
        energyProgress = (ProgressBar) findViewById(R.id.progress_energy_POG);

        firstAnswerImg = (ImageView) findViewById(R.id.first_answer_img);
        secondAnswerImg = (ImageView) findViewById(R.id.second_answer_img);
        thirdAnswerImg = (ImageView) findViewById(R.id.third_answer_img);
        closeTheGame = (ImageView) findViewById(R.id.close_POG);

        firstAnswerTxt.setOnTouchListener(this);
        secondAnswerTxt.setOnTouchListener(this);
        thirdAnswerTxt.setOnTouchListener(this);
        closeTheGame.setOnTouchListener(this);

        //A két progressbarokból levonunk 5-öt
        lifeProgress.setProgress(MainActivity.life.getProgress() - 5);
        energyProgress.setProgress(MainActivity.energy.getProgress() - 5);

        //Megkapjuk a kérdést és kiíratjuk
        getTheCurrentQuestion();

        //A jó válaszok listájának feltöltése
        QuizQuestionFactory.getTheCorrectAnswerList();

        //A telefon rezgetéséhez szükséges vibrátor
         mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Activity üzenetkezelője
        uiHandlerPOG = new Handler() {
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
                    if(!((Activity) PickOneGame.this).isFinishing())
                    {
                    // Saját magát hívja késleltetés után
                    SzelektAkos.increaseGameTime(MSG_GAME_TIME_START);
                    if (SzelektAkos.gameTime <= 500) {
                        uiHandlerPOG.postDelayed(this, GAME_TIME_REFRESHED_TIME);
                    }
                    else {
                            //show dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(PickOneGame.this);
                            builder.setMessage("Gratulálunk!" + "\n" + String.valueOf(reachedPointsPOG) + " pontot szereztél")
                                    .setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Kilép az activity-ből
                                            SzelektAkos.increasePoints(reachedPointsPOG);
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
            uiHandlerPOG.postDelayed(gameTimer, GAME_TIME_REFRESHED_TIME);
    }

    public void getTheCurrentQuestion() {
        QuizQuestionFactory.questionFactory();
        questionTxt.setText(QuizQuestionFactory.getmQuestion());
        firstAnswerTxt.setText(setTheAnswers(QuizQuestionFactory.getmFirstAnswer()));
        secondAnswerTxt.setText(setTheAnswers(QuizQuestionFactory.getmSecondAnswer()));
        thirdAnswerTxt.setText(setTheAnswers(QuizQuestionFactory.getmThirdAnswer()));
    }

    public String setTheAnswers(String textToForm) {
        String refreshedText = textToForm;

        if (textToForm.length() > 30) {
            refreshedText = textToForm.subSequence(0,30).toString() + "...";
        }
        return refreshedText;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.first_answer_txt:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (QuizQuestionFactory.getmCorrectAnswer() == 1) {
                        reachedPointsPOG += 1;
                        reachedPointsTxt.setText(String.valueOf(reachedPointsPOG));
                        getTheCurrentQuestion();
                    } else {
                        mVibrator.vibrate(300);
                        getTheCurrentQuestion();
                    }
                }

                break;

            case R.id.second_answer_txt:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    if (QuizQuestionFactory.getmCorrectAnswer() == 2) {
                        reachedPointsPOG += 1;
                        reachedPointsTxt.setText(String.valueOf(reachedPointsPOG));
                        getTheCurrentQuestion();
                    } else {
                        mVibrator.vibrate(300);
                        getTheCurrentQuestion();
                    }
                }
                break;

            case R.id.third_answer_txt:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    if (QuizQuestionFactory.getmCorrectAnswer() == 3) {
                        reachedPointsPOG += 1;
                        reachedPointsTxt.setText(String.valueOf(reachedPointsPOG));
                        getTheCurrentQuestion();
                    } else {
                        mVibrator.vibrate(300);
                        getTheCurrentQuestion();
                    }
                }
                break;

            case R.id.close_POG:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    SzelektAkos.increasePoints(reachedPointsPOG);
                    gameTimeStopper.cancel(true);
                    finish();
                    break;
                }
        }

        return false;
    }
}
