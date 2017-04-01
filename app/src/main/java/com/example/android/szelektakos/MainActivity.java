package com.example.android.szelektakos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ImageView currentTrouser;
    public static TextView userPointsText;
    public static TextView recentlyPlace;
    public static ProgressBar life;
    public static ProgressBar energy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SzelektAkos.innitApp(getApplicationContext());


        //Felső header inicializálása
        life = (ProgressBar) findViewById(R.id.progress_life);
        energy = (ProgressBar) findViewById(R.id.progress_energy);
        life.setProgress(SzelektAkos.getLife());
        energy.setProgress(SzelektAkos.getEnergy());
        userPointsText = (TextView) findViewById(R.id.user_points);

        recentlyPlace = (TextView) findViewById(R.id.recently_place);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager fm = getSupportFragmentManager();
        Fragment_pager pagerAdapter = new Fragment_pager(fm);
        // A masodik fragmentet tötljük be kezdésként
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);

        //Menubar itemek inicializálása
        ImageView thgWEB = (ImageView) findViewById(R.id.thg_web);
        ImageView shop = (ImageView) findViewById(R.id.shop);
        ImageView pickOneGame = (ImageView) findViewById(R.id.pick_one_game);
        ImageView trashGame = (ImageView) findViewById(R.id.trash_game);
        ImageView wordsGame = (ImageView) findViewById(R.id.words_game);
        ImageView jumpingGame = (ImageView) findViewById(R.id.jumping_game);

        thgWEB.setOnClickListener(this);
        shop.setOnClickListener(this);
        pickOneGame.setOnClickListener(this);
        trashGame.setOnClickListener(this);
        wordsGame.setOnClickListener(this);
        jumpingGame.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        userPointsText.setText(SzelektAkos.getPoints());
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //TODO Le kell menteni a pontot itt a shop tartalmát azt a shopban.
        SzelektAkos.saveAllPrefs();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.thg_web:

            case R.id.shop:
                Intent openShop = new Intent(this, ShopActivity.class);
                startActivity(openShop);
                break;

            case R.id.pick_one_game:

            case R.id.trash_game:

            case R.id.words_game:

            case R.id.jumping_game:



        }


    }

}
