package com.example.android.szelektakos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class THG_Web extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thg__web);

        WebView browser = (WebView) findViewById(R.id.thg_web);
        browser.loadUrl("http://wwww.thgkft.hu");

        ImageView closeTHG = (ImageView) findViewById(R.id.close_THG);
        closeTHG.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
