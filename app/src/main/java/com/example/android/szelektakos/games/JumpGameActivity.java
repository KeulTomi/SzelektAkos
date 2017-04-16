package com.example.android.szelektakos.games;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.android.szelektakos.R;

public class JumpGameActivity extends AppCompatActivity {

    AnimationDrawable akosAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_game);

        ImageView akosRunningView = (ImageView) findViewById(R.id.jump_game_akos);
        akosRunningView.setBackgroundResource(R.drawable.akos_running);

        akosAnimation = (AnimationDrawable) akosRunningView.getBackground();
        akosAnimation.start();
    }
}
