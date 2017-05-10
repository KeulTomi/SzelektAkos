package hu.foxplan.keult.szelektakos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class THG_Web extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thg__web);

        WebView browser = (WebView) findViewById(R.id.thg_web);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl("http://thgkft.hu/");


        ImageView closeTHG = (ImageView) findViewById(R.id.close_THG);
        closeTHG.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        finish();
        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_down);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ScaleHelper.scaleContents(findViewById(R.id.thg_web_root), findViewById(R.id.thg_web_container));
    }
}
