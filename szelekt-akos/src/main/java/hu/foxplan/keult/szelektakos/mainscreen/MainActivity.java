package hu.foxplan.keult.szelektakos.mainscreen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
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

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.ScaleHelper;
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
    public final static int MSG_RESTART_ACTIVITY = 2; // Üzenetkód activity újraindításához
    public static ImageView currentTrouser;
    public static TextView userPointsText;
    public static TextView recentlyPlaceTitle;
    public static ProgressBar life;
    public static ProgressBar energy;
    public static int currentFragmentPage;
    public static Handler uiHandler; // MainActivity üzenetkezelője (onCreate-ben van definiálva)
    public final int ENERGY_REFRESH_PERIOD = 30 * 1000; // Energiaszint frissítési periódusa ezredmásodpercben
    public final int LIFE_REFRESH_PERIOD = 30 * 1000; // Életerő frissítési periódusa ezredmásodpercben
    public ViewPager pager;
    private ImageView rightArrowOfTitle;
    private ImageView leftArrowOfTitle;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //A szükségés innicializálások a későbbi activity-k közti ugrálásokhoz
        SzelektAkos.innitApp(getApplicationContext());

        // Futó szoftver verzió lekérdezése
        int currentVersionCode = 0;

        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Eszközre mentett szoftver verzió előhívása
        int savedVersionCode = SzelektAkos.getVersionCode();

        // Eszközre mentett verziókód ellenőrzése
        if (savedVersionCode == 0) {

            // Lehet, hogy voltak korábban mentett adatok, ezeket is törölni kell
            SzelektAkos.clearAllPrefs();

            // Ha nincs eszközre mentett szoftver verzió (0 az eredmény) akkor most le kell menteni
            SzelektAkos.saveVersionCode(currentVersionCode);

        } else {
            // Van eszközre mentett szoftver verzió
            if (currentVersionCode != savedVersionCode) {

                // Eltér az aktuális és a mentett verzió, ezért törölni kell a mentett (régi) adatokat
                SzelektAkos.clearAllPrefs();

                // És le kell menteni a jelenlegi verziót
                SzelektAkos.saveVersionCode(currentVersionCode);
            }

        }

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
                    case MSG_RESTART_ACTIVITY:
                        Intent intent = getIntent();
                        intent.putExtra("FragmentToLoad", currentFragmentPage);
                        SzelektAkos.saveAllPrefs();
                        finish();

                        startActivity(intent);
                        break;
                    default:
                        // Ha valamilyen más üzenet érkezik, itt lehet lekezelni
                        break;
                }
            }
        };


        //Felső header inicializálása
        life = (ProgressBar) findViewById(R.id.progress_life);
        energy = (ProgressBar) findViewById(R.id.progress_energy);
        life.setProgress(SzelektAkos.getLife());
        energy.setProgress(SzelektAkos.getEnergy());
        userPointsText = (TextView) findViewById(R.id.user_points);
        recentlyPlaceTitle = (TextView) findViewById(R.id.recently_place);
        rightArrowOfTitle = (ImageView) findViewById(R.id.right_arrow);
        leftArrowOfTitle = (ImageView) findViewById(R.id.left_arrow);


        //Beállítás az elmentett értékekre
        life.setProgress(SzelektAkos.life);
        energy.setProgress(SzelektAkos.energy);
        energy.getProgress();

        //A kezdő oldal beállítása
        int fragmentToLoad;

        if (getIntent().hasExtra("FragmentToLoad"))
            fragmentToLoad = getIntent().getIntExtra("FragmentToLoad", 0);
        else
            fragmentToLoad = 1;

        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setOffscreenPageLimit(3);
        FragmentManager fm = getSupportFragmentManager();
        Fragment_pager pagerAdapter = new Fragment_pager(fm);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(fragmentToLoad);

        currentFragmentPage = fragmentToLoad;

        switch (fragmentToLoad) {
            case 0:
                recentlyPlaceTitle.setText("halószoba");
                leftArrowOfTitle.setVisibility(View.INVISIBLE);
                break;
            case 1:
                recentlyPlaceTitle.setText("nappali");
                leftArrowOfTitle.setVisibility(View.VISIBLE);
                rightArrowOfTitle.setVisibility(View.VISIBLE);
                break;
            case 2:
                recentlyPlaceTitle.setText("konyha");
                rightArrowOfTitle.setVisibility(View.INVISIBLE);
                break;
        }

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
        ImageView thgWEB = (ImageView) findViewById(R.id.thg_web);
        ImageView shop = (ImageView) findViewById(R.id.shop);
        ImageView pickOneGame = (ImageView) findViewById(R.id.pick_one_game);
        ImageView trashGame = (ImageView) findViewById(R.id.trash_game);
        ImageView trueFalseGame = (ImageView) findViewById(R.id.true_false_game);
        ImageView wordsGame = (ImageView) findViewById(R.id.words_game);
        ImageView jumpingGame = (ImageView) findViewById(R.id.jumping_game);

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
                        break;
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

        //scaleImageItems();

   }

    @Override
    protected void onResume() {
        userPointsText.setText(SzelektAkos.getPoints());

        if (SzelektAkos.comeBackFromGame) {
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {

            ScaleHelper.scaleContents(
                    findViewById(R.id.main_activity_rootview),
                    findViewById(R.id.main_activity_container),
                    false);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.thg_web:
                Intent openTHGWeb = new Intent(this, THG_Web.class);
                startActivity(openTHGWeb);
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_stay);
                break;

            case R.id.shop:
                Intent openShop = new Intent(this, ShopActivity.class);
                startActivity(openShop);
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_stay);
                break;

            case R.id.pick_one_game:
                Intent playPOG = new Intent(this, PickOneGame.class);
                startActivity(playPOG);
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_stay);
                break;

            case R.id.true_false_game:
                Intent playTFG = new Intent(this, TrueFalseGame.class);
                startActivity(playTFG);
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_stay);
                break;

            case R.id.trash_game:
                Intent playTG = new Intent(this, TrashesGame.class);
                startActivity(playTG);
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_stay);
                break;

            case R.id.words_game:
                Intent playWP = new Intent(this, WordPuzzle.class);
                startActivity(playWP);
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_stay);
                break;

            case R.id.jumping_game:
                Intent playJump = new Intent(this, JumpGameActivity.class);
                startActivity(playJump);
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_stay);
                break;

            case R.id.left_arrow:
                int currentPageLEFT = pager.getCurrentItem();
                pager.setCurrentItem(currentPageLEFT - 1);
                break;

            case R.id.right_arrow:
                int currentPageRIGHT = pager.getCurrentItem();
                pager.setCurrentItem(currentPageRIGHT + 1);
                break;

        }
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

        // Header átméretezése
        imageView = (ImageView) findViewById(R.id.header);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.head2_crop).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.head2_crop).getHeight();
        viewScaleX = SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 0;
        posY = 0;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleX);


        // Menü háttér átméretezése
        imageView = (ImageView) findViewById(R.id.menu_bar);

        akosPosY = 750;


        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.menubar).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.menubar).getHeight();
        viewScaleX = SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = 0.21f * SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);


        // THG Web ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.thg_web);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.picto_web).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.picto_web).getHeight();
        viewScaleX = 0.13f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 12;
        posY = 2010;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Shop ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.shop);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.picto_shop).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.picto_shop).getHeight();
        viewScaleX = 0.13f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 1100;
        posY = 2010;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Pick One game ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.pick_one_game);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.picto_valaszto).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.picto_valaszto).getHeight();
        viewScaleX = 0.17f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 12;
        posY = 2280;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Trash game ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.trash_game);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.picto_kuka).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.picto_kuka).getHeight();
        viewScaleX = 0.17f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 275;
        posY = 2280;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // True-False game ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.true_false_game);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.picto_igaz_hamis).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.picto_igaz_hamis).getHeight();
        viewScaleX = 0.17f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 538;
        posY = 2280;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Words game ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.words_game);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.picto_abc).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.picto_abc).getHeight();
        viewScaleX = 0.17f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 801;
        posY = 2280;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Jump game ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.jumping_game);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.picto_ugralos).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.picto_ugralos).getHeight();
        viewScaleX = 0.17f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 1064;
        posY = 2280;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Balra nyíl pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.left_arrow);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.back_arrow).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.back_arrow).getHeight();
        viewScaleX = 0.1f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 80;
        posY = 330;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Jobbra nyíl pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.right_arrow);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.back_arrow2).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.back_arrow2).getHeight();
        viewScaleX = 0.1f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 1064;
        posY = 330;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Energy ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.energy_picto);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.energy_picto).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.energy_picto).getHeight();
        viewScaleX = 0.056f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 208;
        posY = 95;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Food ikon pozícionálása és méretezése
        imageView = (ImageView) findViewById(R.id.food_picto);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.food_picto).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.food_picto).getHeight();
        viewScaleX = 0.056f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 1008;
        posY = 105;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // User points szöveg pozícionálása (méretezésre nincs szükség)
        TextView textView = (TextView) findViewById(R.id.user_points);
        textView.setTextSize(0.8f * SzelektAkos.displayHeight / textView.getTextSize());

        posX = 537;
        posY = 92;
        //textView.setX(posX * screenScaleX);
        textView.setY(posY * screenScaleY);

        // Pager szöveg pozícionálása (méretezésre nincs szükség)
        textView = (TextView) findViewById(R.id.recently_place);
        textView.setTextSize(0.8f * SzelektAkos.displayHeight / textView.getTextSize());

        posX = 537;
        posY = 345;
        //textView.setX(posX * screenScaleX);
        textView.setY(posY * screenScaleY);

        // Energy progressbar pozícionálása és méretezése
        ProgressBar progressBar;

        progressBar = (ProgressBar) findViewById(R.id.progress_energy);

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
        progressBar = (ProgressBar) findViewById(R.id.progress_life);

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
