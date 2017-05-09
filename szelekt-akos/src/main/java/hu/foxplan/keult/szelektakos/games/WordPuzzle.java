package hu.foxplan.keult.szelektakos.games;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.ScaleHelper;
import hu.foxplan.keult.szelektakos.SzelektAkos;
import hu.foxplan.keult.szelektakos.mainscreen.MainActivity;

public class WordPuzzle extends AppCompatActivity implements View.OnClickListener {

    public final static int MSG_GAME_TIME_START = 7;
    public final static int MSG_GAME_TIME_NULL = 8;
    //Élet és Energia progressek
    public final static int MSG_PROOGRESS_LIFE = 15;
    public final static int MSG_PROOGRESS_ENERGY = 16;
    //GameTime-hoz tartozó változók
    public static Handler uiHandlerWP;
    final int GAME_TIME_REFRESHED_TIME = 100;
    //A sorsolt betűk hátterei
    public ImageView firstLetterBack;
    public ImageView secondLetterBack;
    public ImageView thirdLetterBack;
    public ImageView fourthLetterBack;
    public ImageView fifthLetterBack;
    public ImageView sixthLetterBack;
    public ImageView seventhLetterBack;
    public ImageView eighthLetterBack;
    public ImageView ninthLetterBack;
    //A tippelt betűk hátterei
    public ImageView firstGuesstLetterBack;
    public ImageView secondGuesstLetterBack;
    public ImageView thirdGuesstLetterBack;
    public ImageView fourthGuesstLetterBack;
    public ImageView fifthGuesstLetterBack;
    public ImageView sixthGuesstLetterBack;
    //A tippelt betűk üres helyei
    public ImageView firstGuesstLetterPlc;
    public ImageView secondGuesstLetterPlc;
    public ImageView thirdGuesstLetterPlc;
    public ImageView fourthGuesstLetterPlc;
    public ImageView fifthGuesstLetterPlc;
    public ImageView sixthGuesstLetterPlc;
    //A tippelt betűk layoutjai
    public RelativeLayout firstGuesstLetterLayout;
    public RelativeLayout secondGuesstLetterLayout;
    public RelativeLayout thirdGuesstLetterLayout;
    public RelativeLayout fourthGuesstLetterLayout;
    public RelativeLayout fifthGuesstLetterLayout;
    public RelativeLayout sixthGuesstLetterLayout;
    //A sorsolt betűk
    public TextView firstLetter;
    public TextView secondLetter;
    public TextView thirdLetter;
    public TextView fourthLetter;
    public TextView fifthLetter;
    public TextView sixthLetter;
    public TextView seventhLetter;
    public TextView eighthLetter;
    public TextView ninthLetter;
    //Sorsolt betűl layoutjai
    public RelativeLayout firstLetterLayout;
    public RelativeLayout secondLetterLayout;
    public RelativeLayout thirdLetterLayout;
    public RelativeLayout fourthLetterLayout;
    public RelativeLayout fifthLetterLayout;
    public RelativeLayout sixthLetterLayout;
    public RelativeLayout seventhLetterLayout;
    public RelativeLayout eighthLetterLayout;
    public RelativeLayout ninthLetterLayout;
    //A tippelt betűk
    public TextView firstGuessLetter;
    public TextView secondGuessLetter;
    public TextView thirdGuessLetter;
    public TextView fourthGuessLetter;
    public TextView fifthGuessLetter;
    public TextView sixthGuessLetter;
    //A kérdés text-je
    public TextView questionText;
    //Bezerás gomb
    public ImageView closeWP;
    //Arrayek
    public TextView[] txtViewList;
    public ArrayList dinamixTxtViewList;
    public TextView[] guessTxtViewList;
    public ArrayList dinamicGuessImgViewListPlc;
    public ArrayList dinamicGuessTxtViewList;
    public ImageView[] guessImgViewListPlc;
    public ImageView[] guessImgViewListBack;
    public String[] getLettersTextList;
    public boolean[] choosenLetters = new boolean[9];
    //A jó váasz betűinek teszteléséhez a változók
    public String firstCorrectL;
    public String secondCorrectL;
    public String thirdCorrectL;
    public String fourthCorrectL;
    public String fifthCorrectL;
    public String sixthCorrectL;
    public String correctWord;
    //Egyéb változók
    int previousPlace;
    boolean haveEmptyPlace;
    int reachedPointsWP;
    TextView reachedPointText;
    Vibrator mVibrator;
    private Future gameTimeStopper;
    private ProgressBar gameTimeProgress;
    private ProgressBar lifeProgress;
    private ProgressBar energyProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hu.foxplan.keult.szelektakos.R.layout.activity_word_puzzle);

        //A kepernyő összes Text-jének és Image-ének az inicializálása

        firstLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.first_letter_back);
        secondLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.second_letter_back);
        thirdLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.third_letter_back);
        fourthLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.fourth_letter_back);
        fifthLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.fifth_letter_back);
        sixthLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.sixth_letter_back);
        seventhLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.seventh_letter_back);
        eighthLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.eighth_letter_back);
        ninthLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.ninth_letter_back);

        firstLetterLayout = (RelativeLayout) findViewById(R.id.first_letter_layout);
        secondLetterLayout = (RelativeLayout) findViewById(R.id.second_letter_layout);
        thirdLetterLayout = (RelativeLayout) findViewById(R.id.third_letter_layout);
        fourthLetterLayout = (RelativeLayout) findViewById(R.id.fourth_letter_layout);
        fifthLetterLayout = (RelativeLayout) findViewById(R.id.fifth_letter_layout);
        sixthLetterLayout = (RelativeLayout) findViewById(R.id.sixth_letter_layout);
        seventhLetterLayout = (RelativeLayout) findViewById(R.id.seventh_letter_layout);
        eighthLetterLayout = (RelativeLayout) findViewById(R.id.eighth_letter_layout);
        ninthLetterLayout = (RelativeLayout) findViewById(R.id.ninth_letter_layout);

        firstGuesstLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_first_letter_back);
        secondGuesstLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_second_letter_back);
        thirdGuesstLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_third_letter_back);
        fourthGuesstLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_fourth_letter_back);
        fifthGuesstLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_fifth_letter_back);
        sixthGuesstLetterBack = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_sixth_letter_back);

        firstGuesstLetterPlc = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_first_letter_plc);
        secondGuesstLetterPlc = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_second_letter_plc);
        thirdGuesstLetterPlc = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_third_letter_plc);
        fourthGuesstLetterPlc = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_fourth_letter_plc);
        fifthGuesstLetterPlc = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_fifth_letter_plc);
        sixthGuesstLetterPlc = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_sixth_letter_plc);

        firstGuesstLetterLayout = (RelativeLayout) findViewById(R.id.guess_first_letter_layout);
        secondGuesstLetterLayout = (RelativeLayout) findViewById(R.id.guess_second_letter_layout);
        thirdGuesstLetterLayout = (RelativeLayout) findViewById(R.id.guess_third_letter_layout);
        fourthGuesstLetterLayout = (RelativeLayout) findViewById(R.id.guess_fourth_letter_layout);
        fifthGuesstLetterLayout = (RelativeLayout) findViewById(R.id.guess_fifth_letter_layout);
        sixthGuesstLetterLayout = (RelativeLayout) findViewById(R.id.guess_sixth_letter_layout);

        firstLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.first_letter);
        secondLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.second_letter);
        thirdLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.third_letter);
        fourthLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.fourth_letter);
        fifthLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.fifth_letter);
        sixthLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.sixth_letter);
        seventhLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.seventh_letter);
        eighthLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.eighth_letter);
        ninthLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.ninth_letter);

        firstGuessLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_first_letter);
        secondGuessLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_second_letter);
        thirdGuessLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_third_letter);
        fourthGuessLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_fourth_letter);
        fifthGuessLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_fifth_letter);
        sixthGuessLetter = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.guess_sixth_letter);

        questionText = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.question_txt_WP);
        closeWP = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.close_WP);
        gameTimeProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.game_time_progress_WP);
        lifeProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.progress_life_WP);
        energyProgress = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.progress_energy_WP);
        reachedPointText = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.reached_points_txt_WP);

        firstLetterLayout.setOnClickListener(this);
        secondLetterLayout.setOnClickListener(this);
        thirdLetterLayout.setOnClickListener(this);
        fourthLetterLayout.setOnClickListener(this);
        fifthLetterLayout.setOnClickListener(this);
        sixthLetterLayout.setOnClickListener(this);
        seventhLetterLayout.setOnClickListener(this);
        eighthLetterLayout.setOnClickListener(this);
        ninthLetterLayout.setOnClickListener(this);

        firstGuesstLetterLayout.setOnClickListener(this);
        secondGuesstLetterLayout.setOnClickListener(this);
        thirdGuesstLetterLayout.setOnClickListener(this);
        fourthGuesstLetterLayout.setOnClickListener(this);
        fifthGuesstLetterLayout.setOnClickListener(this);
        sixthGuesstLetterLayout.setOnClickListener(this);

        closeWP.setOnClickListener(this);

        //A két progressbarokból levonunk 5-öt
        lifeProgress.setProgress(MainActivity.life.getProgress() - 5);
        energyProgress.setProgress(MainActivity.energy.getProgress() - 5);

        //A telefon rezgetéséhez szükséges vibrátor
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Megkapjuk az következő kérdést és az ezzel járó feladatokat elvégezzük
        nextQuestion();

        // Activity üzenetkezelője
        uiHandlerWP = new Handler() {
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
                if (!WordPuzzle.this.isFinishing())
                {
                    // Saját magát hívja késleltetés után
                    SzelektAkos.increaseGameTime(MSG_GAME_TIME_START);
                    if (SzelektAkos.gameTime <= 813) {
                        uiHandlerWP.postDelayed(this, GAME_TIME_REFRESHED_TIME);
                    }
                    else {
                        //show dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(WordPuzzle.this);
                        builder.setMessage("Gratulálunk!" + "\n" + String.valueOf(reachedPointsWP) + " pontot szereztél")
                                .setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Kilép az activity-ből
                                        SzelektAkos.increasePoints(reachedPointsWP);
                                        SzelektAkos.comeBackFromGame = true;
                                        gameTimeStopper.cancel(true);
                                        dialog.dismiss();
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
        uiHandlerWP.post(lifeProgresser);
        uiHandlerWP.post(energyProgresser);
        uiHandlerWP.postDelayed(gameTimer, GAME_TIME_REFRESHED_TIME);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            //Ha a választható betűkre kattint
            case R.id.first_letter_layout:
                String firstString = firstLetter.getText().toString();
                setThePickedLetter(firstString, firstLetter, 0);
                break;

            case R.id.second_letter_layout:
                String secondString = secondLetter.getText().toString();
                setThePickedLetter(secondString, secondLetter, 1);
                break;

            case R.id.third_letter_layout:
                String thirdString = thirdLetter.getText().toString();
                setThePickedLetter(thirdString, thirdLetter, 2);
                break;

            case R.id.fourth_letter_layout:
                String fourthString = fourthLetter.getText().toString();
                setThePickedLetter(fourthString, fourthLetter, 3);
                break;

            case R.id.fifth_letter_layout:
                String fifthString = fifthLetter.getText().toString();
                setThePickedLetter(fifthString, fifthLetter, 4);
                break;

            case R.id.sixth_letter_layout:
                String sixthString = sixthLetter.getText().toString();
                setThePickedLetter(sixthString, sixthLetter, 5);
                break;

            case R.id.seventh_letter_layout:
                String seventhString = seventhLetter.getText().toString();
                setThePickedLetter(seventhString, seventhLetter, 6);
                break;

            case R.id.eighth_letter_layout:
                String eighthString = eighthLetter.getText().toString();
                setThePickedLetter(eighthString, eighthLetter, 7);
                break;

            case R.id.ninth_letter_layout:
                String ninthString = ninthLetter.getText().toString();
                setThePickedLetter(ninthString, ninthLetter, 8);
                break;


            //Ha vissza szeretne vonni egyet a már kiválasztott betűkből
            case R.id.guess_first_letter_layout:
                if (firstGuessLetter.getText() != "") {
                    String guessFirstString = firstGuessLetter.getText().toString();
                    removeALetter(guessFirstString, firstGuessLetter, 0);
                }
                break;

            case R.id.guess_second_letter_layout:
                if (secondGuessLetter.getText() != "") {
                    String guessSecondString = secondGuessLetter.getText().toString();
                    removeALetter(guessSecondString, secondGuessLetter, 1);
                }
                break;

            case R.id.guess_third_letter_layout:
                if (thirdGuessLetter.getText() != "") {
                    String guessThirdString = thirdGuessLetter.getText().toString();
                    removeALetter(guessThirdString, thirdGuessLetter, 2);
                }
                break;

            case R.id.guess_fourth_letter_layout:
                if (fourthGuessLetter.getText() != "") {
                    String guessFourthString = fourthGuessLetter.getText().toString();
                    removeALetter(guessFourthString, fourthGuessLetter, 3);
                }
                break;

            case R.id.guess_fifth_letter_layout:
                if (fifthGuessLetter.getText() != "") {
                    String guessFifthString = fifthGuessLetter.getText().toString();
                    removeALetter(guessFifthString, fifthGuessLetter, 4);
                }
                break;

            case R.id.guess_sixth_letter_layout:
                if (sixthGuessLetter.getText() != "") {
                    String guessSixthString = sixthGuessLetter.getText().toString();
                    removeALetter(guessSixthString, sixthGuessLetter, 5);
                }
                break;

            case hu.foxplan.keult.szelektakos.R.id.close_WP:
                SzelektAkos.increasePoints(reachedPointsWP);
                SzelektAkos.comeBackFromGame = true;
                finish();
        }
    }

    public void nextQuestion () {

        //Megkapjuk a kovetkező kérdést és a hozzá tartozó szót
        WordFactory.innitWordAndQuestions();
        //Kiiratjuk az aktuális kérdést
        questionText.setText(WordFactory.mCurrentQuestion);

        //Inicializálások a helyes szó betűinek a szétszórásához
        Random r1 = new Random();
        txtViewList = new TextView[]{firstLetter, secondLetter , thirdLetter, fourthLetter, fifthLetter
                ,sixthLetter, seventhLetter, eighthLetter, ninthLetter};

        guessImgViewListPlc = new ImageView[]{firstGuesstLetterPlc, secondGuesstLetterPlc, thirdGuesstLetterPlc, fourthGuesstLetterPlc
                ,fifthGuesstLetterPlc, sixthGuesstLetterPlc};

        guessImgViewListBack = new ImageView[]{firstGuesstLetterBack, secondGuesstLetterBack, thirdGuesstLetterBack, fourthGuesstLetterBack
        ,fifthGuesstLetterBack, sixthGuesstLetterBack};

        guessTxtViewList = new TextView[]{firstGuessLetter, secondGuessLetter, thirdGuessLetter, fourthGuessLetter, fifthGuessLetter
                ,sixthGuessLetter};

        dinamixTxtViewList = new ArrayList<TextView>(Arrays.asList(txtViewList));
        dinamicGuessImgViewListPlc = new ArrayList<ImageView>();
        dinamicGuessTxtViewList = new ArrayList<TextView>();

        int letterPlaceNum;
        String currentLetter;
        TextView letterTextView;
        int index = 0;

        //Addig keressük a random helyeket a helyes szó betűinek amíg el nem fogynak a betűk
        while (index <= (WordFactory.mCurrentWord.length() - 1)){
            if (index == 6){
                return;
            }
            currentLetter = Character.toString(WordFactory.mCurrentWord.charAt(index));
            letterPlaceNum = r1.nextInt(dinamixTxtViewList.size());
            letterTextView = (TextView) dinamixTxtViewList.get(letterPlaceNum);
            letterTextView.setText(currentLetter);
            dinamicGuessImgViewListPlc.add(guessImgViewListPlc[index]);
            dinamicGuessTxtViewList.add(guessTxtViewList[index]);
            guessImgViewListPlc[index].setVisibility(View.VISIBLE);
            guessImgViewListBack[index].setVisibility(View.VISIBLE);
            dinamixTxtViewList.remove(letterPlaceNum);
            index++;
        }

        //Inicializálások a maradék hely betöltésére az abc betűivel
        Random r2 = new Random();
        int abcLetterPlaceNum;
        TextView abcLetterTextView;
        String abcLetter;
        String[] ABC = {"a","á","b","c","d","e","é","f","g","h","i","í","j","k","l","m","n","o","ó","ö","ő"
                ,"p","r","s","t","u","ú","ü","ű","v","z"};

        //Addig sorsolunk az abc betűi közül és helyezzük el a random TextView-kon amíg van hely nekik
        //Vagyis amíg a dinamicTxtViewList nem nulla
        while (dinamixTxtViewList.size() == 0){
            abcLetterPlaceNum = r2.nextInt(dinamixTxtViewList.size());
            abcLetter = ABC[abcLetterPlaceNum];
            abcLetterTextView = (TextView) dinamixTxtViewList.get(abcLetterPlaceNum);
            abcLetterTextView.setText(abcLetter);
            dinamixTxtViewList.remove(abcLetterPlaceNum);
        }
    }

    public void setThePickedLetter (String pickedLetter, TextView pickedLetterText, int choosenLetterNum){
        int placesNumber = 0;


        while (guessTxtViewList[placesNumber].getText() != ""){
            placesNumber++;
            if (placesNumber == 6) return;
            if (placesNumber == (dinamicGuessTxtViewList.size())) return;
        }

        TextView emptyPlace;
        ImageView emptyPlaceImg;

        if (guessTxtViewList[placesNumber].getText() == ""){

            if (choosenLetters[choosenLetterNum] == false) {

                choosenLetters[choosenLetterNum] = true;
                emptyPlace = guessTxtViewList[placesNumber];
                emptyPlaceImg = (ImageView) dinamicGuessImgViewListPlc.get(placesNumber);
                emptyPlaceImg.setVisibility(View.INVISIBLE);
                pickedLetterText.setTextColor(Color.GRAY);
                emptyPlace.setText(pickedLetter);

                switch (placesNumber){
                    case 0:
                        firstCorrectL = pickedLetter;
                        break;
                    case 1:
                        secondCorrectL = pickedLetter;
                        break;
                    case 2:
                        thirdCorrectL = pickedLetter;
                        break;
                    case 3:
                        fourthCorrectL = pickedLetter;
                        break;
                    case 4:
                        fifthCorrectL = pickedLetter;
                        break;
                    case 5:
                        sixthCorrectL = pickedLetter;
                        break;
                }
                int index = 0;
                String[] letters = {firstCorrectL, secondCorrectL, thirdCorrectL, fourthCorrectL, fifthCorrectL, sixthCorrectL};
                while (index <=5){
                    if (letters[index] != null) {
                        if (index == 0){
                            correctWord = letters[index];
                        }
                        else {
                            correctWord += letters[index];
                        }
                    }
                    index++;
                }
                if (correctWord.matches( WordFactory.mCurrentWord)){
                    ArrayList<String> correctLettersList= new ArrayList<String>(Arrays.asList(letters));
                    int index2 = WordFactory.mCurrentWord.length() - 1;

                    while (index2 != -1){
                        removeALetter(correctLettersList.get(index2), guessTxtViewList[index2], index2);
                        correctLettersList.remove(index2);
                        index2--;
                    }
                    guessImgViewListBack[0].setVisibility(View.INVISIBLE);
                    guessImgViewListBack[1].setVisibility(View.INVISIBLE);
                    guessImgViewListBack[2].setVisibility(View.INVISIBLE);
                    guessImgViewListBack[3].setVisibility(View.INVISIBLE);
                    guessImgViewListBack[4].setVisibility(View.INVISIBLE);
                    guessImgViewListBack[5].setVisibility(View.INVISIBLE);

                    guessImgViewListPlc[0].setVisibility(View.INVISIBLE);
                    guessImgViewListPlc[1].setVisibility(View.INVISIBLE);
                    guessImgViewListPlc[2].setVisibility(View.INVISIBLE);
                    guessImgViewListPlc[3].setVisibility(View.INVISIBLE);
                    guessImgViewListPlc[4].setVisibility(View.INVISIBLE);
                    guessImgViewListPlc[5].setVisibility(View.INVISIBLE);

                    reachedPointsWP += 1;
                    reachedPointText.setText(String.valueOf(reachedPointsWP));
                    nextQuestion();
                }
                else {
                    if (correctWord.length() == WordFactory.mCurrentWord.length()) {

                        mVibrator.vibrate(300);
                    }
                }
            }
            else {
                return;
            }
        }
    }

    public void removeALetter(String letterForRemove, TextView letterFRPlace, int removeLetterNum){

        getLettersTextList = new String[]{firstLetter.getText().toString(), secondLetter.getText().toString(), thirdLetter.getText().toString()
        ,fourthLetter.getText().toString(), fifthLetter.getText().toString(), sixthLetter.getText().toString(), seventhLetter.getText().toString()
        ,eighthLetter.getText().toString(), ninthLetter.getText().toString()};

        txtViewList = new TextView[]{firstLetter, secondLetter , thirdLetter, fourthLetter, fifthLetter
                ,sixthLetter, seventhLetter, eighthLetter, ninthLetter};

        previousPlace = 0;
        ImageView choosenImageView;
        searchThePreLetter(letterForRemove);

        if (haveEmptyPlace){
            letterFRPlace.setText("");
            choosenImageView = (ImageView) dinamicGuessImgViewListPlc.get(removeLetterNum);
            choosenImageView.setVisibility(View.VISIBLE);
            txtViewList[previousPlace].setTextColor(Color.BLACK);
            choosenLetters[previousPlace] = false;
            switch (removeLetterNum){
                case 0:
                    firstCorrectL = "";
                    break;
                case 1:
                    secondCorrectL = "";
                    break;
                case 2:
                    thirdCorrectL = "";
                    break;
                case 3:
                    fourthCorrectL = "";
                    break;
                case 4:
                    fifthCorrectL = "";
                    break;
                case 5:
                    sixthCorrectL = "";
                    break;
            }
        }
    }

    public void searchThePreLetter(String letterForRemove) {
        if (previousPlace > 8) {
            haveEmptyPlace = false;
            return;
        }
        while (letterForRemove != getLettersTextList[previousPlace]) {
            previousPlace++;
        }
        if (choosenLetters[previousPlace] == false) {
            previousPlace++;
            searchThePreLetter(letterForRemove);
        }
        haveEmptyPlace = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ScaleHelper.scaleContents(findViewById(R.id.words_game_root), findViewById(R.id.words_game_container));
    }
}
