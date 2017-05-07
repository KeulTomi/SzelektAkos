package hu.foxplan.keult.szelektakos.games;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.ScaleHelper;
import hu.foxplan.keult.szelektakos.SzelektAkos;
import hu.foxplan.keult.szelektakos.mainscreen.MainActivity;

public class PickOneGame extends AppCompatActivity implements View.OnTouchListener {

    public final static int MSG_GAME_TIME_START = 0;
    public final static int MSG_GAME_TIME_NULL = 1;
    public final static int MSG_PROOGRESS_LIFE = 9;
    public final static int MSG_PROOGRESS_ENERGY = 10;
    public static Handler uiHandlerPOG;
    public final int GAME_TIME_REFRESHED_TIME = 100;
    //TODO A gombnyomásra ki kell cserélni a képeket.
    public ImageView firstAnswerImg;
    public ImageView secondAnswerImg;
    private TextView questionTxt;
    private TextView firstAnswerTxt;
    private TextView secondAnswerTxt;
    private TextView thirdAnswerTxt;
    private TextView reachedPointsTxt;
    private RelativeLayout firstAnswerTxtLayout;
    private RelativeLayout secondAnswerTxtLayout;
    private RelativeLayout thirdAnswerTxtLayout;
    private ProgressBar gameTimeProgress;
    private ProgressBar lifeProgress;
    private ProgressBar energyProgress;
    private ImageView thirdAnswerImg;
    private ImageView closeTheGame;
    private int reachedPointsPOG;
    private Vibrator mVibrator;
    private Future gameTimeStopper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hu.foxplan.keult.szelektakos.R.layout.activity_pick_one_game);

        //Kerdéshez szükséges Textek és képek innit
        questionTxt = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.question_txt_POG);
        firstAnswerTxt = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.first_answer_txt);
        secondAnswerTxt = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.second_answer_txt);
        thirdAnswerTxt = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.third_answer_txt);
        reachedPointsTxt = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.reached_points_txt_POG);
        gameTimeProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.game_time_progress_POG);
        lifeProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.progress_life_POG);
        energyProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.progress_energy_POG);

        firstAnswerImg = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.first_answer_img);
        secondAnswerImg = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.second_answer_img);
        thirdAnswerImg = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.third_answer_img);
        closeTheGame = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.close_POG);

        firstAnswerTxtLayout = (RelativeLayout) findViewById(R.id.first_answer_txt_layout);
        secondAnswerTxtLayout = (RelativeLayout) findViewById(R.id.second_answer_txt_layout);
        thirdAnswerTxtLayout = (RelativeLayout) findViewById(R.id.third_answer_txt_layout);

        firstAnswerTxtLayout.setOnTouchListener(this);
        secondAnswerTxtLayout.setOnTouchListener(this);
        thirdAnswerTxtLayout.setOnTouchListener(this);
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
                    if (!PickOneGame.this.isFinishing())
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
                                            SzelektAkos.comeBackFromGame = true;
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
            uiHandlerPOG.post(lifeProgresser);
            uiHandlerPOG.post(energyProgresser);
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
            case R.id.first_answer_txt_layout:
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

            case R.id.second_answer_txt_layout:
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

            case R.id.third_answer_txt_layout:
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

            case hu.foxplan.keult.szelektakos.R.id.close_POG:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    SzelektAkos.increasePoints(reachedPointsPOG);
                    SzelektAkos.comeBackFromGame = true;
                    gameTimeStopper.cancel(true);
                    finish();
                    break;
                }
        }

        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ScaleHelper.scaleContents(findViewById(R.id.pick_one_game_root), findViewById(R.id.pick_one_game_container));
    }
}
