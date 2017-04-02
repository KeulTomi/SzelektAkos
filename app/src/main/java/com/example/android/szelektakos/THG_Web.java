package com.example.android.szelektakos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class THG_Web extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thg__web);

        WebView browser = (WebView) findViewById(R.id.thg_web);
        browser.loadUrl("http://thgkft.hu");

    }
}
