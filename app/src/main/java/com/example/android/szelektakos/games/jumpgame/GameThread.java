package com.example.android.szelektakos.games.jumpgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.SurfaceHolder;

import com.example.android.szelektakos.R;
import com.example.android.szelektakos.SzelektAkos;

import java.util.Random;

/**
 * A játékot vezérlő Thread
 */

class GameThread extends Thread {

    public static boolean isGameRunning;
    public static boolean jumpRequest = false;
    private final int GAP_BETWEEN_BASE_PLATFORMS = 15;
    private Handler mViewHandler;
    private Context mViewContext;
    private SurfaceHolder mSurfaceHolder;
    private JumpMan mJumpMan; // Ugró figura objektuma
    private Platform[] mBasePlatform; // Alapjárda objektuma
    private Platform mUpperPlatform; // Felső járda objektuma
    private Platform mBottomPlatform; // Alsó járda objektuma
    private Platform mBluePlatform; // Kék színű ugrójárda objektuma
    private Bitmap mBackgroundPic; // Játék háttérképe
    private int mDisplayWidth;
    private int mDisplayHeight;
    private int UPPER_PLATFORM_YPOS; // Felső járda y pozíciója
    private int BOTTOM_PLATFORM_YPOS; // Alsó járda y pozíciója
    private int BASE_PLATFORM_YPOS; // Alapjárda (padló) y pozíciója
    private int PLATFORM_HEIGHT; // Járda magassága
    private int JUMPMAN_HEIGHT; // Ugráló figura magassága
    private int JUMPMAN_UPPER_LIMIT_YPOS; // Ugráló figura legmagasabb megengedet felső y pozíciója
    private int JUMPMAN_BOTTOM_LIMIT_YPOS; // Ugráló figura legalacsonyabb megengedet alsó y pozíciója
    private int JUMPMAN_XPOS; // Ugráló figura x pozíciója
    private int goodItemsCollected;
    private int badItemsCollected;


    GameThread(Context context, Handler viewHandler, SurfaceHolder surfaceHolder) {
        // Paraméterek mentése
        if (context != null)
            this.mViewContext = context;
        if (viewHandler != null)
            this.mViewHandler = viewHandler;
        if (surfaceHolder != null)
            this.mSurfaceHolder = surfaceHolder;
    }

    static void jumpRequest() {
        jumpRequest = true;
    }

    @Override
    public void run() {

        // Játék Thread-je

        initGame();

        // Animációk indítása
        mJumpMan.startAnimation();
        mBasePlatform[0].startAnimation();
        mBasePlatform[1].startAnimation();
        mBasePlatform[2].startAnimation();
        mUpperPlatform.startAnimation();
        mBottomPlatform.startAnimation();


        while (isGameRunning) {
            if (!mSurfaceHolder.getSurface().isValid())
                continue;

            Canvas canvas = mSurfaceHolder.lockCanvas();
            Point jumpManPos = new Point(mJumpMan.getPos());
            Rect rectUndrerPlatform = new Rect();
            boolean isUpperPosLimited = false;
            boolean isBottomPosLimited = false;

            if (jumpRequest) {
                mJumpMan.jump();
                jumpRequest = false;
            }

            // Felső járda alatti téglalap vizsgálata, hogy benne van-e a figura
            // ha alatta van a figura akkor a felugrást kell korlátozni
            rectUndrerPlatform.set(
                    mUpperPlatform.getPos().x,
                    mUpperPlatform.getPos().y,
                    mUpperPlatform.getPos().x + mUpperPlatform.getWidth(),
                    BASE_PLATFORM_YPOS);

            if (rectUndrerPlatform.contains(jumpManPos.x, jumpManPos.y)) {
                // Ugrásmagasság korlátozása felülről, mert járda van felette
                mJumpMan.setPosUpperLimit(mUpperPlatform.getPos().y + PLATFORM_HEIGHT);
                isUpperPosLimited = true;
            }

            // Alsó járda alatti téglalap vizsgálata, hogy benne van-e a figura
            // ha alatta van a figura akkor a felugrást kell korlátozni
            rectUndrerPlatform.set(
                    mBottomPlatform.getPos().x,
                    mBottomPlatform.getPos().y,
                    mBottomPlatform.getPos().x + mBottomPlatform.getWidth(),
                    BASE_PLATFORM_YPOS);

            if (rectUndrerPlatform.contains(jumpManPos.x + mJumpMan.getWidth(), jumpManPos.y)) {
                // Ugrásmagasság korlátozása felülről, mert járda van felette
                mJumpMan.setPosUpperLimit(mBottomPlatform.getPos().y + PLATFORM_HEIGHT);
                isUpperPosLimited = true;
            }

            // Kék ugró járda alatti téglalap vizsgálata, hogy benne van-e a figura
            // ha alatta van a figura akkor a felugrást kell korlátozni
            rectUndrerPlatform.set(
                    mBluePlatform.getPos().x,
                    mBluePlatform.getPos().y,
                    mBluePlatform.getPos().x + mBluePlatform.getWidth(),
                    BASE_PLATFORM_YPOS);

            if (rectUndrerPlatform.contains(jumpManPos.x + mJumpMan.getWidth(), jumpManPos.y)) {
                // Ugrásmagasság korlátozása felülről, mert járda van felette
                mJumpMan.setPosUpperLimit(mBluePlatform.getPos().y + PLATFORM_HEIGHT);
                isUpperPosLimited = true;
            }

            if (!isUpperPosLimited)
                // Nincs a figura fölött járda ugrásmagasságot nem kell korlátozni
                mJumpMan.setPosUpperLimit(JUMPMAN_UPPER_LIMIT_YPOS);

            // Alsó járda fölötti téglalap vizsgálata, hogy benne van-e a figura
            // ha fölötte van a figura akkor az alső (leesési) limitet kell a járda szintre állítani
            rectUndrerPlatform.set(
                    mBottomPlatform.getPos().x,
                    0,
                    mBottomPlatform.getPos().x + mBottomPlatform.getWidth() + mJumpMan.getWidth() / 2,
                    mBottomPlatform.getPos().y);

            if (rectUndrerPlatform.contains(jumpManPos.x + mJumpMan.getWidth(), jumpManPos.y)) {
                // Alsó (leesési) limit beállítása a járdaszintre, mert mozgó járda van alatta
                mJumpMan.setPosBottomLimit(BOTTOM_PLATFORM_YPOS - JUMPMAN_HEIGHT);
                isBottomPosLimited = true;
            }

            // Kék ugró járda fölötti téglalap vizsgálata, hogy benne van-e a figura
            // ha fölötte van a figura akkor az alső (leesési) limitet kell a járda szintre állítani
            rectUndrerPlatform.set(
                    mBluePlatform.getPos().x,
                    0,
                    mBluePlatform.getPos().x + mBluePlatform.getWidth() + mJumpMan.getWidth() / 2,
                    mBluePlatform.getPos().y);

            if (rectUndrerPlatform.contains(jumpManPos.x + mJumpMan.getWidth(), jumpManPos.y)) {
                // Alsó (leesési) limit beállítása a járdaszintre, mert mozgó járda van alatta
                mJumpMan.setPosBottomLimit(BOTTOM_PLATFORM_YPOS - JUMPMAN_HEIGHT);
                isBottomPosLimited = true;
            }

            //
            // Felső járda fölötti téglalap vizsgálata, hogy benne van-e a figura
            // ha fölötte van a figura akkor az alső (leesési) limitet kell a járda szintre állítani
            //
            rectUndrerPlatform.set(
                    mUpperPlatform.getPos().x,
                    0,
                    mUpperPlatform.getPos().x + mUpperPlatform.getWidth() + mJumpMan.getWidth() / 2,
                    mUpperPlatform.getPos().y);

            if (rectUndrerPlatform.contains(jumpManPos.x + mJumpMan.getWidth(), jumpManPos.y)) {
                // Alsó (leesési) limit beállítása a járdaszintre, mert mozgó járda van alatta
                mJumpMan.setPosBottomLimit(UPPER_PLATFORM_YPOS - JUMPMAN_HEIGHT);
                isBottomPosLimited = true;
            }

            if (!isBottomPosLimited)
                // Nincs alatta járda, alsó (leesési) limit beállítása a padlószintre
                mJumpMan.setPosBottomLimit(JUMPMAN_BOTTOM_LIMIT_YPOS);

            //
            // Háttérkép kirajzolása
            //
            if (mBackgroundPic != null)
                canvas.drawBitmap(mBackgroundPic, 0, 0, null);

            //
            // Padlók kezelése
            //
            for (int num = 0; num < 3; num++) {

                if (mBasePlatform[num] != null) {

                    // Figura és Item találkozásának ellenőrzése
                    checkInterference(mBasePlatform[num], jumpManPos);

                    // Járda balra eltűnésének ellenőrzése
                    if (mBasePlatform[num].getPos().x <= (-1 * mBasePlatform[num].getWidth())) {

                        // Ha a járda már nem látszik a képernyőn újra lehet hasznosítani,
                        // pozícionálás véletlenszerűen a képernyő szélességen túlra
                        switch (num) {
                            case 0:
                                // Az első járda már nem látszik, mehet a végére
                                mBasePlatform[0].setPos(
                                        mBasePlatform[2].getPos().x +
                                                mBasePlatform[2].getWidth() +
                                                GAP_BETWEEN_BASE_PLATFORMS, null);
                                break;

                            case 1:
                                // A második járda már nem látszik, mehet a végére
                                mBasePlatform[1].setPos(
                                        mBasePlatform[0].getPos().x +
                                                mBasePlatform[0].getWidth() +
                                                GAP_BETWEEN_BASE_PLATFORMS, null);
                                break;
                            case 2:
                                // A harmadik járda már nem látszik, mehet a végére
                                mBasePlatform[2].setPos(
                                        mBasePlatform[1].getPos().x +
                                                mBasePlatform[1].getWidth() +
                                                GAP_BETWEEN_BASE_PLATFORMS, null);
                                break;
                        }

                        // Újabb elemeket lehet rádobni
                        mBasePlatform[num].dropItems(2);
                    }

                    // Járda rajzolása
                    canvas.drawBitmap(mBasePlatform[num].getBmp(), mBasePlatform[num].getPos().x, mBasePlatform[num].getPos().y, null);

                    // Item-ek rajzolása
                    for (int i = 0; i < 2; i++) {
                        if (mBasePlatform[num].isItemValid(i)) {
                            canvas.drawBitmap(
                                    mBasePlatform[num].getItemBmp(i),
                                    mBasePlatform[num].getPos().x + mBasePlatform[num].getItemXpos(i),
                                    mBasePlatform[num].getPos().y - mBasePlatform[num].getItemHeight(i),
                                    null);
                        }
                    }
                }

            }

            //
            // Felső járda kezelése
            //
            if (mUpperPlatform != null) {

                // Figura és Item találkozásának ellenőrzése
                checkInterference(mUpperPlatform, jumpManPos);

                // Járda balra eltűnésének ellenőrzése
                if (mUpperPlatform.getPos().x <= (-1 * mUpperPlatform.getWidth())) {

                    // Ha a járda már nem látszik a képernyőn újra lehet hasznosítani,
                    // pozícionálás véletlenszerűen a képernyő szélességen túlra
                    mUpperPlatform.setPos(getNextRandomPos(mDisplayWidth, mDisplayWidth), null);

                    // Újabb elemeket lehet rádobni
                    mUpperPlatform.dropItems(2);
                }

                // Járda rajzolása
                canvas.drawBitmap(mUpperPlatform.getBmp(), mUpperPlatform.getPos().x, mUpperPlatform.getPos().y, null);

                // Item-ek rajzolása
                for (int i = 0; i < 2; i++) {
                    if (mUpperPlatform.isItemValid(i)) {
                        canvas.drawBitmap(
                                mUpperPlatform.getItemBmp(i),
                                mUpperPlatform.getPos().x + mUpperPlatform.getItemXpos(i),
                                mUpperPlatform.getPos().y - mUpperPlatform.getItemHeight(i),
                                null);
                    }
                }
            }

            //
            // Alsó járda kezelése
            //
            if (mBottomPlatform != null) {

                // Figura és Item találkozásának ellenőrzése
                checkInterference(mBottomPlatform, jumpManPos);

                // Járda balra eltűnésének ellenőrzése
                if (mBottomPlatform.getPos().x <= (-1 * mBottomPlatform.getWidth())) {

                    // Ha a járda már nem látszik a képernyőn újra lehet hasznosítani,
                    // pozícionálás véletlenszerűen a képernyő szélességen túlra
                    mBottomPlatform.setPos(getNextRandomPos(mDisplayWidth, mDisplayWidth), null);

                    // Újabb elemeket lehet rádobni
                    mBottomPlatform.dropItems(2);
                }

                // Járda rajzolása
                canvas.drawBitmap(mBottomPlatform.getBmp(), mBottomPlatform.getPos().x, mBottomPlatform.getPos().y, null);

                // Item-ek rajzolása
                for (int i = 0; i < 2; i++) {
                    if (mBottomPlatform.isItemValid(i)) {
                        canvas.drawBitmap(
                                mBottomPlatform.getItemBmp(i),
                                mBottomPlatform.getPos().x + mBottomPlatform.getItemXpos(i),
                                mBottomPlatform.getPos().y - mBottomPlatform.getItemHeight(i),
                                null);
                    }
                }

            }


            //
            // Kék ugró járda kezelése
            //
            if (mBluePlatform != null) {

                // Járda balra eltűnésének ellenőrzése
                if (mBluePlatform.getPos().x <= (-1 * mBluePlatform.getWidth())) {

                    // Ha a járda már nem látszik a képernyőn újra lehet hasznosítani,
                    // pozícionálás véletlenszerűen a képernyő szélességen túlra
                    mBluePlatform.setPos(getNextRandomPos(2 * mDisplayWidth, mDisplayWidth), null);
                }

                // Járda rajzolása
                canvas.drawBitmap(mBluePlatform.getBmp(), mBluePlatform.getPos().x, mBluePlatform.getPos().y, null);

            }

            if (mJumpMan != null)
                canvas.drawBitmap(mJumpMan.getBmp(), mJumpMan.getPos().x, mJumpMan.getPos().y, null);

            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }

        // Thread leáll objektumok megsemmisítése
        mJumpMan = null;
        mBasePlatform = null;
        mUpperPlatform = null;
        mBottomPlatform = null;
    }

    private void checkInterference(Platform platform, Point jumpManPos) {

        Point itemPos = new Point(0, 0);

        // Mindkét elem ellenőrzése
        for (int i = 0; i < 2; i++) {

            if (platform.isItemValid(i)) {

                itemPos.x = platform.getPos().x + platform.getItemXpos(i);
                itemPos.y = platform.getPos().y - platform.getItemHeight(i);

                // Együttállás ellenőrzése
                if (itemPos.y >= jumpManPos.y && itemPos.y <= (jumpManPos.y + mJumpMan.getHeight()))
                    if (itemPos.x >= jumpManPos.x && itemPos.x <= (jumpManPos.x + mJumpMan.getWidth())) {
                        // Item-et talált
                        processItemFound(platform, i);
                    }
            }
        }

    }

    private void processItemFound(Platform platform, int itemPos) {

        Vibrator vibrator = (Vibrator) mViewContext.getSystemService(Context.VIBRATOR_SERVICE);
        Message msgToActivity = new Message();

        if (platform.isItemBad(itemPos)) {
            vibrator.vibrate(100);
            badItemsCollected++;
            if (badItemsCollected >= 5) {
                isGameRunning = false;
                msgToActivity.what = JumpGameActivity.MSG_GAME_OVER;
                msgToActivity.arg1 = goodItemsCollected;
                JumpGameActivity.uiHandlerJG.sendMessage(msgToActivity);
            }
        } else {
            goodItemsCollected++;
            msgToActivity.what = JumpGameActivity.MSG_POINTS_COLLECTED;
            msgToActivity.arg1 = goodItemsCollected;
            JumpGameActivity.uiHandlerJG.sendMessage(msgToActivity);
        }

        platform.invalidateItem(itemPos);
    }

    private void initGame() {

        mDisplayWidth = SzelektAkos.displayWidth;
        mDisplayHeight = SzelektAkos.displayHeight;

        UPPER_PLATFORM_YPOS = (int) (0.4762 * mDisplayHeight); // Felső járda y pozíciója
        BOTTOM_PLATFORM_YPOS = (int) (0.599 * mDisplayHeight); // Alsó járda y pozíciója
        BASE_PLATFORM_YPOS = (int) (0.819 * mDisplayHeight); // Alapjárda (padló) y pozíciója
        PLATFORM_HEIGHT = (int) (0.0572 * mDisplayHeight); // Járda magassága
        JUMPMAN_HEIGHT = (int) (0.1523 * mDisplayHeight); // Ugráló figura magassága
        JUMPMAN_XPOS = (int) (0.1 * mDisplayWidth); // Ugráló figura x pozíciója

        // Pontok inicializálása
        goodItemsCollected = 0;
        badItemsCollected = 0;

        // Háttér inicializálása és méretezése
        Bitmap bitmap = BitmapFactory.decodeResource(mViewContext.getResources(), R.drawable.jump_game_background);

        mBackgroundPic = Bitmap.createScaledBitmap(
                bitmap,
                mDisplayWidth,
                mDisplayHeight,
                false);

        // Véletlenszerűen a járdákra dobott tárgyak betöltése a Platform objektumba
        Platform.loadItemResources(mViewContext);

        //
        // Felső járda inicializálása és méretezése
        //
        Bitmap greenPlatform = BitmapFactory.decodeResource(mViewContext.getResources(), R.drawable.jump_game_green_platform);

        mUpperPlatform = new Platform(
                greenPlatform,
                PLATFORM_HEIGHT,
                mViewHandler);

        mUpperPlatform.setPos(
                getNextRandomPos(mDisplayWidth, mDisplayWidth),
                UPPER_PLATFORM_YPOS);

        mUpperPlatform.dropItems(2);

        //
        // Alsó járda inicializálása és méretezése
        //
        mBottomPlatform = new Platform(
                greenPlatform,
                PLATFORM_HEIGHT,
                mViewHandler);

        mBottomPlatform.setPos(
                getNextRandomPos(mDisplayWidth, (int) (mDisplayWidth * 1.5)),
                BOTTOM_PLATFORM_YPOS);

        mBottomPlatform.dropItems(2);

        //
        // Kék színű ugró járda
        //

        Bitmap bluePlatform = BitmapFactory.decodeResource(mViewContext.getResources(), R.drawable.jump_game_blue_platform);

        mBluePlatform = new Platform(
                bluePlatform,
                PLATFORM_HEIGHT,
                mViewHandler);

        mBluePlatform.setPos(
                getNextRandomPos(2 * mDisplayWidth, (int) (mDisplayWidth * 1.5)),
                BOTTOM_PLATFORM_YPOS);


        //
        // Padló járda példányainak inicializálása és méretezése
        //
        mBasePlatform = new Platform[3];

        mBasePlatform[0] = new Platform(
                BitmapFactory.decodeResource(mViewContext.getResources(), R.drawable.jump_game_base_platform),
                PLATFORM_HEIGHT,
                mViewHandler);

        mBasePlatform[0].setPos(
                (int) (0),
                BASE_PLATFORM_YPOS);

        mBasePlatform[0].dropItems(2);

        mBasePlatform[1] = new Platform(
                BitmapFactory.decodeResource(mViewContext.getResources(), R.drawable.jump_game_base_platform),
                PLATFORM_HEIGHT,
                mViewHandler);

        mBasePlatform[1].setPos(
                (int) (mBasePlatform[0].getWidth() + GAP_BETWEEN_BASE_PLATFORMS),
                BASE_PLATFORM_YPOS);

        mBasePlatform[1].dropItems(2);

        mBasePlatform[2] = new Platform(
                BitmapFactory.decodeResource(mViewContext.getResources(), R.drawable.jump_game_base_platform),
                PLATFORM_HEIGHT,
                mViewHandler);

        mBasePlatform[2].setPos(
                (int) (mBasePlatform[1].getPos().x + mBasePlatform[1].getWidth() + GAP_BETWEEN_BASE_PLATFORMS),
                BASE_PLATFORM_YPOS);

        mBasePlatform[2].dropItems(2);


        //
        // Ugró figura inicializálása és méretezése
        //
        Integer[] jumpManResourceArray = new Integer[11];
        jumpManResourceArray[0] = R.drawable.akos_run_01;
        jumpManResourceArray[1] = R.drawable.akos_run_02;
        jumpManResourceArray[2] = R.drawable.akos_run_03;
        jumpManResourceArray[3] = R.drawable.akos_run_04;
        jumpManResourceArray[4] = R.drawable.akos_run_05;
        jumpManResourceArray[5] = R.drawable.akos_run_06;
        jumpManResourceArray[6] = R.drawable.akos_run_07;
        jumpManResourceArray[7] = R.drawable.akos_run_08;
        jumpManResourceArray[8] = R.drawable.akos_run_09;
        jumpManResourceArray[9] = R.drawable.akos_run_10;
        jumpManResourceArray[10] = R.drawable.akos_run_11;

        mJumpMan = new JumpMan(
                jumpManResourceArray,
                JUMPMAN_HEIGHT,
                mViewHandler,
                mViewContext);

        mJumpMan.setPos(
                JUMPMAN_XPOS,
                BASE_PLATFORM_YPOS - mJumpMan.getHeight());

        JUMPMAN_UPPER_LIMIT_YPOS = 0; //UPPER_PLATFORM_YPOS - mJumpMan.getHeight() - 3 * mUpperPlatform.getHeight();
        JUMPMAN_BOTTOM_LIMIT_YPOS = (int) (BASE_PLATFORM_YPOS - mJumpMan.getHeight());
        mJumpMan.setPosBottomLimit(JUMPMAN_BOTTOM_LIMIT_YPOS);
        mJumpMan.setPosUpperLimit(JUMPMAN_UPPER_LIMIT_YPOS);

    }

    private int getNextRandomPos(int min, int maxDistance) {
        Random random = new Random();
        return min + random.nextInt(maxDistance);
    }

}
