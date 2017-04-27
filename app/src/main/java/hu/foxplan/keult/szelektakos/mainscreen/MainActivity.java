package hu.foxplan.keult.szelektakos.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import hu.foxplan.keult.szelektakos.SzelektAkos;
import hu.foxplan.keult.szelektakos.THG_Web;
import hu.foxplan.keult.szelektakos.games.PickOneGame;
import hu.foxplan.keult.szelektakos.games.TrashesGame;
import hu.foxplan.keult.szelektakos.games.TrueFalseGame;
import hu.foxplan.keult.szelektakos.games.WordPuzzle;
import hu.foxplan.keult.szelektakos.games.jumpgame.JumpGameActivity;
import hu.foxplan.keult.szelektakos.shop.ShopActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static int MSG_UPDATE_LIFE = 0; // Üzenetkód életerő progressbar frissítéséhez
    public final static int MSG_UPDATE_ENERGY = 1; // Üzenetkód energia progressbar frissítéséhez
    public static ImageView currentTrouser;
    public static TextView userPointsText;
    public static TextView recentlyPlaceTitle;
    public static ProgressBar life;
    public static ProgressBar energy;
    public static int currentFragmentPage;
    public static Handler uiHandler; // MainActivity üzenetkezelője (onCreate-ben van definiálva)
    public final int ENERGY_REFRESH_PERIOD = 30 * 1000; // Energiaszint frissítési periódusa ezredmásodpercben
    public final int LIFE_REFRESH_PERIOD = 30 * 1000; // Életerő frissítési periódusa ezredmásodpercben
    private ViewPager pager;
    private ImageView rightArrowOfTitle;
    private ImageView leftArrowOfTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hu.foxplan.keult.szelektakos.R.layout.activity_main);

        // Activity üzenetkezelője
        uiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_UPDATE_LIFE:
                        // Életerő frissítése üzenet érkezett
                        life.setProgress((Integer) msg.obj); // Életerő frissítése msg.obj-ben küldött értékkel
                        break;
                    case MSG_UPDATE_ENERGY:
                        energy.setProgress((Integer) msg.obj); // Energia frissítése msg.obj-ben küldött értékkel
                        break;
                    default:
                        // Ha valamilyen más üzenet érkezik, itt lehet lekezelni
                        break;
                }
            }
        };

        //A szükségés innicializálások a későbbi activity-k közti ugrálásokhoz
        SzelektAkos.innitApp(getApplicationContext());

        //Felső header inicializálása
        life = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.progress_life);
        energy = (ProgressBar) findViewById(hu.foxplan.keult.szelektakos.R.id.progress_energy);
        life.setProgress(SzelektAkos.getLife());
        energy.setProgress(SzelektAkos.getEnergy());
        userPointsText = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.user_points);
        recentlyPlaceTitle = (TextView) findViewById(hu.foxplan.keult.szelektakos.R.id.recently_place);
        rightArrowOfTitle = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.right_arrow);
        leftArrowOfTitle = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.left_arrow);

        //A kezdő oldalt a lapszámláló egyesként jelzo ezért hogy ne nullával számoljon be kell állíani 1-re
        currentFragmentPage = 1;

        //Beállítás az elmentett értékekre
        life.setProgress(SzelektAkos.life);
        energy.setProgress(SzelektAkos.energy);
        energy.getProgress();

        //A fő viewpager összeállítása
        pager = (ViewPager) findViewById(hu.foxplan.keult.szelektakos.R.id.view_pager);
        FragmentManager fm = getSupportFragmentManager();
        Fragment_pager pagerAdapter = new Fragment_pager(fm);

        // A masodik fragmentet tötljük be kezdésként
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);

        //A ViewPager_re pagecChangeListenert teszünk, hogy oldalváltásoknál cseréljük a recentlyPlaceTitle-t.
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        currentFragmentPage = position;
                        recentlyPlaceTitle.setText("halószoba");
                        leftArrowOfTitle.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        currentFragmentPage = position;
                        recentlyPlaceTitle.setText("nappali");
                        leftArrowOfTitle.setVisibility(View.VISIBLE);
                        rightArrowOfTitle.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        currentFragmentPage = position;
                        recentlyPlaceTitle.setText("konyha");
                        rightArrowOfTitle.setVisibility(View.INVISIBLE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Menubar itemek inicializálása
        ImageView thgWEB = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.thg_web);
        ImageView shop = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.shop);
        ImageView pickOneGame = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.pick_one_game);
        ImageView trashGame = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.trash_game);
        ImageView trueFalseGame = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.true_false_game);
        ImageView wordsGame = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.words_game);
        ImageView jumpingGame = (ImageView) findViewById(hu.foxplan.keult.szelektakos.R.id.jumping_game);

        thgWEB.setOnClickListener(this);
        shop.setOnClickListener(this);
        pickOneGame.setOnClickListener(this);
        trueFalseGame.setOnClickListener(this);
        trashGame.setOnClickListener(this);
        wordsGame.setOnClickListener(this);
        jumpingGame.setOnClickListener(this);

        rightArrowOfTitle.setOnClickListener(this);
        leftArrowOfTitle.setOnClickListener(this);

        // Energiaszint időzítő futtatható kódja, ez fut le minden időzítés után
        Runnable energyTimer = new Runnable() {
            @Override
            public void run() {
                switch (currentFragmentPage) {
                    case 0:
                        // Hálószobára lapozott, energiaszint növekszik
                        SzelektAkos.changeEnergy(1);
                        break;
                    case 1:
                        // Nappalira lapozott, energiaszint csökken
                        SzelektAkos.changeEnergy(-1);
                    case 2:
                        // Konyhára lapozott, energiaszint csökken
                        SzelektAkos.changeEnergy(-1);
                        break;
                }
                // Saját magát hívja késleltetés után
                uiHandler.postDelayed(this, ENERGY_REFRESH_PERIOD);
            }
        };

        // Életerő időzítő futtatható kódja, ez fut le minden időzítés után
        final Runnable lifeTimer = new Runnable() {
            @Override
            public void run() {
                // Életerő csökkentése
                SzelektAkos.changeLifeValue(-1);
                // Saját magát hívja késleltetés után
                uiHandler.postDelayed(this, LIFE_REFRESH_PERIOD);
            }
        };

        // Időzítők beindítása (első futtatás)
        uiHandler.postDelayed(energyTimer, ENERGY_REFRESH_PERIOD);
        uiHandler.postDelayed(lifeTimer, LIFE_REFRESH_PERIOD);
   }

    @Override
    protected void onResume() {
        userPointsText.setText(SzelektAkos.getPoints());

        if (SzelektAkos.comeBackFromGame == true) {
            SzelektAkos.changeLifeValue(-5);
            SzelektAkos.changeEnergy(-5);
            SzelektAkos.comeBackFromGame = false;
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //TODO Le kell menteni a pontot itt a shop tartalmát azt a shopban.
        SzelektAkos.energy = energy.getProgress();
        SzelektAkos.life = life.getProgress();
        SzelektAkos.saveAllPrefs();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case hu.foxplan.keult.szelektakos.R.id.thg_web:
                Intent openTHGWeb = new Intent(this, THG_Web.class);
                startActivity(openTHGWeb);
                break;

            case hu.foxplan.keult.szelektakos.R.id.shop:
                Intent openShop = new Intent(this, ShopActivity.class);
                startActivity(openShop);
                break;

            case hu.foxplan.keult.szelektakos.R.id.pick_one_game:
                Intent playPOG = new Intent(this, PickOneGame.class);
                startActivity(playPOG);
                break;

            case hu.foxplan.keult.szelektakos.R.id.true_false_game:
                Intent playTFG = new Intent(this, TrueFalseGame.class);
                startActivity(playTFG);
                break;

            case hu.foxplan.keult.szelektakos.R.id.trash_game:
                Intent playTG = new Intent(this, TrashesGame.class);
                startActivity(playTG);
                break;

            case hu.foxplan.keult.szelektakos.R.id.words_game:
                Intent playWP = new Intent(this, WordPuzzle.class);
                startActivity(playWP);
                break;

            case hu.foxplan.keult.szelektakos.R.id.jumping_game:
                Intent playJump = new Intent(this, JumpGameActivity.class);
                startActivity(playJump);
                break;

            case hu.foxplan.keult.szelektakos.R.id.left_arrow:
                int currentPageLEFT = pager.getCurrentItem();
                pager.setCurrentItem(currentPageLEFT - 1);
                break;

            case hu.foxplan.keult.szelektakos.R.id.right_arrow:
                int currentPageRIGHT = pager.getCurrentItem();
                pager.setCurrentItem(currentPageRIGHT + 1);
                break;

        }
    }
}
