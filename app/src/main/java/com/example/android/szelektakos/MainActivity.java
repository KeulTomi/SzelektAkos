package com.example.android.szelektakos;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager fm = getSupportFragmentManager();
        Fragment_Pager pagerAdapter = new Fragment_Pager(fm);
        // Here you would declare which page to visit on creation
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);
    }
}
