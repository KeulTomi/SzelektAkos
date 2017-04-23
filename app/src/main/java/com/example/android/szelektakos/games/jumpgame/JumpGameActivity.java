package com.example.android.szelektakos.games.jumpgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.android.szelektakos.R;

public class JumpGameActivity extends AppCompatActivity implements View.OnTouchListener {

    JumpGameView jumpGameView;
    ImageView gameCloseVew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_game);

        jumpGameView = (JumpGameView) findViewById(R.id.jump_game_surface_view);
        jumpGameView.setOnTouchListener(this);

        gameCloseVew = (ImageView) findViewById(R.id.jump_game_close);
        gameCloseVew.setOnTouchListener(this);
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
                finish();
                break;
            case R.id.jump_game_surface_view:
                jumpGameView.click();
                break;
        }

        return false;
    }
}
