package com.example.android.szelektakos;

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
import android.widget.TextView;

public class PickOneGame extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTxt;
    private TextView firstAnswerTxt;
    private TextView secondAnswerTxt;
    private TextView thirdAnswerTxt;
    private TextView reachedPointsTxt;
    private ProgressBar gameTimeProgress;
    private ImageView firstAnswerImg;
    private ImageView secondAnswerImg;
    private ImageView thirdAnswerImg;
    private ImageView closeTheGame;
    private QuizQuestionFactory questionFactory;
    private int reachedPoints;
    private Vibrator mVibrator;
    final static int MSG_GAME_TIME = 0;
    final int GAME_TIME_REFRESHED_TIME = 100;
    public static Handler uiHandlerPOG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_one_game);

        //Kerdéshez szükséges Textek és képek innit
        questionTxt = (TextView) findViewById(R.id.question_txt);
        firstAnswerTxt = (TextView) findViewById(R.id.first_answer_txt);
        secondAnswerTxt = (TextView) findViewById(R.id.second_answer_txt);
        thirdAnswerTxt = (TextView) findViewById(R.id.third_answer_txt);
        reachedPointsTxt = (TextView) findViewById(R.id.reached_points_txt);
        gameTimeProgress = (ProgressBar) findViewById(R.id.game_time_progress);

        firstAnswerImg = (ImageView) findViewById(R.id.first_answer_img);
        secondAnswerImg = (ImageView) findViewById(R.id.second_answer_img);
        thirdAnswerImg = (ImageView) findViewById(R.id.third_answer_img);
        closeTheGame = (ImageView) findViewById(R.id.close_POG);

        firstAnswerTxt.setOnClickListener(this);
        secondAnswerTxt.setOnClickListener(this);
        thirdAnswerTxt.setOnClickListener(this);
        closeTheGame.setOnClickListener(this);

        //Megkapjuk a kérdést és kiíratjuk
        getTheCurrentQuestion();

        //A telefon rezgetéséhez szükséges vibrátor
         mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Activity üzenetkezelője
        uiHandlerPOG = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_GAME_TIME:
                        gameTimeProgress.setProgress((Integer) msg.obj);
                        break;
                    default:
                        // Ha valamilyen más üzenet érkezik, itt lehet lekezelni
                        break;
                }
            }
        };

        // Életerő időzítő futtatható kódja, ez fut le minden időzítés után
//        if (SzelektAkos.gameTime <= 520) {

            final Runnable gameTimer = new Runnable() {
                @Override
                public void run() {
                    // Saját magát hívja késleltetés után
                    SzelektAkos.increaseGameTime();
                    if (SzelektAkos.gameTime <= 500) {
                        uiHandlerPOG.postDelayed(this, GAME_TIME_REFRESHED_TIME);
                    }
                    else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Gratulálunk!" + String.valueOf(reachedPoints) + "pontot szereztél")
                        .setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Kilép az activity-ből
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                    }
                }
            };

            // Időzítők beindítása (első futtatás)
            uiHandlerPOG.postDelayed(gameTimer, GAME_TIME_REFRESHED_TIME);
        //}
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.first_answer_txt:
                if (questionFactory.getmCorrectAnswer() == 1) {
                    reachedPoints += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPoints));
                    getTheCurrentQuestion();
                }
                else {
                    mVibrator.vibrate(300);
                    getTheCurrentQuestion();
                }
                break;

            case R.id.second_answer_txt:
                if (questionFactory.getmCorrectAnswer() == 2) {
                    reachedPoints += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPoints));
                    getTheCurrentQuestion();
                }
                else {
                    mVibrator.vibrate(300);
                    getTheCurrentQuestion();
                }
                break;

            case R.id.third_answer_txt:
                if (questionFactory.getmCorrectAnswer() == 3) {
                    reachedPoints += 1;
                    reachedPointsTxt.setText(String.valueOf(reachedPoints));
                    getTheCurrentQuestion();
                }
                else {
                    mVibrator.vibrate(300);
                    getTheCurrentQuestion();
                }
                break;

            case R.id.close_POG:
                SzelektAkos.increasePoints(reachedPoints);
                finish();
                break;
        }
    }

    public QuizQuestionFactory getTheCurrentQuestion() {
        questionFactory = QuizQuestionFactory.questionFactory();
        questionTxt.setText(questionFactory.getmQuestion());
        firstAnswerTxt.setText(questionFactory.getmFirstAnswer());
        secondAnswerTxt.setText(questionFactory.getmSecondAnswer());
        thirdAnswerTxt.setText(questionFactory.getmThirdAnswer());
        return questionFactory;
    }
}
