package hu.foxplan.keult.szelektakos.games.jumpgame;

import android.content.DialogInterface;
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
import hu.foxplan.keult.szelektakos.ScaleHelper;
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
    int firstWindow = 0;
    int reachedPoints = 0;
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
                        reachedPoints++;
                        break;
                    case MSG_GAME_OVER:
                        // popup ablak megjelenítése
                        reachedPoints = msg.arg1;
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
                                        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_down);
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

//        scaleImageItems();
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
                SzelektAkos.increasePoints(reachedPoints);
                finish();
                overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_down);
                break;
            case R.id.jump_game_surface_view:
                jumpGameView.click();
                break;
        }

        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ScaleHelper.scaleContents(
                findViewById(R.id.activity_jump_root),
                findViewById(R.id.activity_jump_container),
                false);

        if (firstWindow == 0) {
            if (energyProgress.getProgress() <= 0) {
//            if (!PickOneGame.this.isFinishing()) {
//            }
//            else {
                //show dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(JumpGameActivity.this);
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
