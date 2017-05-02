package hu.foxplan.keult.szelektakos.games.jumpgame;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.SzelektAkos;
import hu.foxplan.keult.szelektakos.mainscreen.MainActivity;

public class JumpGameActivity extends AppCompatActivity implements View.OnTouchListener {

    public final static int MSG_PROOGRESS_LIFE = 17;
    public final static int MSG_PROOGRESS_ENERGY = 18;
    public final static int MSG_POINTS_COLLECTED = 19;
    public final static int MSG_GAME_OVER = 20;
    public static Handler uiHandlerJG;
    public static TextView pointsTextView;
    JumpGameView jumpGameView;
    ImageView gameCloseVew;
    private ProgressBar lifeProgress;
    private ProgressBar energyProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_game);

        pointsTextView = (TextView) findViewById(R.id.jump_game_reached_points_txt);

        //A két progressbarokból levonunk 5-öt

        lifeProgress = (ProgressBar) findViewById(R.id.jump_game_progress_life);
        energyProgress = (ProgressBar) findViewById(R.id.jump_game_progress_energy);

        lifeProgress.setProgress(MainActivity.life.getProgress() - 5);
        energyProgress.setProgress(MainActivity.energy.getProgress() - 5);


        // Activity üzenetkezelője
        uiHandlerJG = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_PROOGRESS_LIFE:
                        lifeProgress.setProgress((Integer) msg.obj);
                        break;

                    case MSG_PROOGRESS_ENERGY:
                        energyProgress.setProgress((Integer) msg.obj);
                        break;
                    case MSG_POINTS_COLLECTED:
                        pointsTextView.setText(Integer.toString(msg.arg1));
                        break;
                    case MSG_GAME_OVER:
                        // popup ablak megjelenítése
                        final int reachedPoints = msg.arg1;
                        AlertDialog.Builder builder = new AlertDialog.Builder(JumpGameActivity.this);
                        builder
                                .setCancelable(false)
                                .setMessage("Gratulálunk!" + "\n" + String.valueOf(reachedPoints) + " pontot szereztél")
                                .setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Kilép az activity-ből
                                        SzelektAkos.increasePoints(reachedPoints);
                                        SzelektAkos.comeBackFromGame = true;
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();

                        alert.show();
                        break;
                    default:
                        // Ha valamilyen más üzenet érkezik, itt lehet lekezelni
                        break;
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

        // Időzítők beindítása (első futtatás)
        uiHandlerJG.post(lifeProgresser);
        uiHandlerJG.post(energyProgresser);

        jumpGameView = (JumpGameView) findViewById(R.id.jump_game_surface_view);
        jumpGameView.setOnTouchListener(this);

        gameCloseVew = (ImageView) findViewById(R.id.jump_game_close);
        gameCloseVew.setOnTouchListener(this);

        scaleImageItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jumpGameView.pause(); // Esemény továbbítása a grafikát kezelő SurfaceView-nak
    }

    @Override
    protected void onResume() {
        super.onResume();
        jumpGameView.resume(); // Esemény továbbítása a grafikát kezelő SurfaceView-nak
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.jump_game_close:
                //SzelektAkos.increasePoints(reachedPointsPOG);
                SzelektAkos.comeBackFromGame = true;
                finish();
                break;
            case R.id.jump_game_surface_view:
                jumpGameView.click();
                break;
        }

        return false;
    }

    private void scaleImageItems() {

        // Méretarány kiszámítása
        float viewScaleX;
        float viewScaleY;
        float screenScaleX = SzelektAkos.displayWidth / 1300.0f;
        float screenScaleY = SzelektAkos.displayHeight / 2558.0f;
        float akosPosY;
        int origWidth;
        int origHeight;
        int posX;
        int posY;
        ImageView imageView;

        // User points szöveg pozícionálása (méretezésre nincs szükség)
        TextView textView = (TextView) findViewById(R.id.jump_game_reached_points_txt);
        textView.setTextSize(0.8f * SzelektAkos.displayHeight / textView.getTextSize());

        posX = 537;
        posY = 92;
        //textView.setX(posX * screenScaleX);
        textView.setY(posY * screenScaleY);


        // Energy ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.jump_game_energy_picto);

        origWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.energy_picto).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.mipmap.energy_picto).getHeight();
        viewScaleX = 0.056f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 208;
        posY = 95;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Food ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.jump_game_food_picto);

        origWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.food_picto).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.mipmap.food_picto).getHeight();
        viewScaleX = 0.056f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 1008;
        posY = 105;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Game close ikon pozícionálása és méretezése
        View view = findViewById(R.id.jump_game_close);


        origWidth = 100;
        origHeight = 100;
        viewScaleX = 0.1f * SzelektAkos.displayWidth / (float) origWidth;

        view.getLayoutParams().width = (int) (origWidth * viewScaleX);
        view.getLayoutParams().height = (int) (origHeight * viewScaleX);

        // Energy progressbar pozícionálása és méretezése
        ProgressBar progressBar;

        progressBar = (ProgressBar) findViewById(R.id.jump_game_progress_energy);

        origWidth = 400;
        origHeight = 85;
        viewScaleX = 0.305f * SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = 0.07f * SzelektAkos.displayHeight / (float) origHeight;

        progressBar.getLayoutParams().width = (int) (origWidth * viewScaleX);
        progressBar.getLayoutParams().height = (int) (origHeight * viewScaleY);

        posX = 52;
        posY = 77;
        progressBar.setX(posX * screenScaleX);
        progressBar.setY(posY * screenScaleY);

        // Life progressbar pozícionálása és méretezése
        progressBar = (ProgressBar) findViewById(R.id.jump_game_progress_life);

        origWidth = 400;
        origHeight = 85;
        viewScaleX = 0.305f * SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = 0.07f * SzelektAkos.displayHeight / (float) origHeight;

        progressBar.getLayoutParams().width = (int) (origWidth * viewScaleX);
        progressBar.getLayoutParams().height = (int) (origHeight * viewScaleY);

        posX = 850;
        posY = 77;
        progressBar.setX(posX * screenScaleX);
        progressBar.setY(posY * screenScaleY);
    }
}
