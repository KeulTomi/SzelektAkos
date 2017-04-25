package com.example.android.szelektakos.games.jumpgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
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

            //
            // Felső járda alatti téglalap vizsgálata, hogy benne van-e a figura
            //
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

            //
            // Alsó járda alatti téglalap vizsgálata, hogy benne van-e a figura
            //
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

            if (!isUpperPosLimited)
                // Nincs a figura fölött járda ugrásmagasságot nem kell korlátozni
                mJumpMan.setPosUpperLimit(JUMPMAN_UPPER_LIMIT_YPOS);

            //
            // Alsó járda fölötti téglalap vizsgálata, hogy benne van-e a figura
            //
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

            //
            // Felső járda fölötti téglalap vizsgálata, hogy benne van-e a figura
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

            // Háttérkép kirajzolása
            if (mBackgroundPic != null)
                canvas.drawBitmap(mBackgroundPic, 0, 0, null);

            //
            // Alapjárdák egymás után rajzolása
            //
            if (mBasePlatform[0] != null) {
                if (mBasePlatform[0].getPos().x <= (-1 * mBasePlatform[0].getWidth())) {
                    // Az első járda már nem látszik, mehet a végére
                    mBasePlatform[0].setPos(
                            mBasePlatform[2].getPos().x +
                                    mBasePlatform[2].getWidth() +
                                    GAP_BETWEEN_BASE_PLATFORMS, null);

                    // Újabb elemeket lehet rádobni
                    mBasePlatform[0].dropItems(2);
                }

                canvas.drawBitmap(mBasePlatform[0].getBmp(), mBasePlatform[0].getPos().x, mBasePlatform[0].getPos().y, null);

                if (mBasePlatform[0].isItemValid(0))
                    canvas.drawBitmap(
                            mBasePlatform[0].getItemBmp(0),
                            mBasePlatform[0].getPos().x + mBasePlatform[0].getItemXpos(0),
                            mBasePlatform[0].getPos().y - mBasePlatform[0].getItemHeight(0),
                            null);

                if (mBasePlatform[0].isItemValid(1))
                    canvas.drawBitmap(
                            mBasePlatform[0].getItemBmp(1),
                            mBasePlatform[0].getPos().x + mBasePlatform[0].getItemXpos(1),
                            mBasePlatform[0].getPos().y - mBasePlatform[0].getItemHeight(1),
                            null);
            }

            if (mBasePlatform[1] != null) {
                if (mBasePlatform[1].getPos().x <= (-1 * mBasePlatform[1].getWidth())) {
                    // A második járda már nem látszik, mehet a végére
                    mBasePlatform[1].setPos(
                            mBasePlatform[0].getPos().x +
                                    mBasePlatform[0].getWidth() +
                                    GAP_BETWEEN_BASE_PLATFORMS, null);

                    // Újabb elemeket lehet rádobni
                    mBasePlatform[1].dropItems(2);
                }

                canvas.drawBitmap(mBasePlatform[1].getBmp(), mBasePlatform[1].getPos().x, mBasePlatform[1].getPos().y, null);

                if (mBasePlatform[1].isItemValid(0))
                    canvas.drawBitmap(
                            mBasePlatform[1].getItemBmp(0),
                            mBasePlatform[1].getPos().x + mBasePlatform[1].getItemXpos(0),
                            mBasePlatform[1].getPos().y - mBasePlatform[1].getItemHeight(0),
                            null);

                if (mBasePlatform[1].isItemValid(1))
                    canvas.drawBitmap(
                            mBasePlatform[1].getItemBmp(1),
                            mBasePlatform[1].getPos().x + mBasePlatform[1].getItemXpos(1),
                            mBasePlatform[1].getPos().y - mBasePlatform[1].getItemHeight(1),
                            null);
            }

            if (mBasePlatform[2] != null) {
                if (mBasePlatform[2].getPos().x <= (-1 * mBasePlatform[2].getWidth())) {
                    // A harmadik járda már nem látszik, mehet a végére
                    mBasePlatform[2].setPos(
                            mBasePlatform[1].getPos().x +
                                    mBasePlatform[1].getWidth() +
                                    GAP_BETWEEN_BASE_PLATFORMS, null);

                    // Újabb elemeket lehet rádobni
                    mBasePlatform[2].dropItems(2);
                }

                canvas.drawBitmap(mBasePlatform[2].getBmp(), mBasePlatform[2].getPos().x, mBasePlatform[2].getPos().y, null);

                if (mBasePlatform[2].isItemValid(0))
                    canvas.drawBitmap(
                            mBasePlatform[2].getItemBmp(0),
                            mBasePlatform[2].getPos().x + mBasePlatform[2].getItemXpos(0),
                            mBasePlatform[2].getPos().y - mBasePlatform[2].getItemHeight(0),
                            null);

                if (mBasePlatform[2].isItemValid(1))
                    canvas.drawBitmap(
                            mBasePlatform[2].getItemBmp(1),
                            mBasePlatform[2].getPos().x + mBasePlatform[2].getItemXpos(1),
                            mBasePlatform[2].getPos().y - mBasePlatform[2].getItemHeight(1),
                            null);
            }

            //
            // Felső járda kirajzolása
            //
            if (mUpperPlatform != null) {

                if (mUpperPlatform.getPos().x <= (-1 * mUpperPlatform.getWidth())) {
                    mUpperPlatform.setPos(getNextRandomPos(mDisplayWidth, mDisplayWidth), null);

                    // Újabb elemeket lehet rádobni
                    mUpperPlatform.dropItems(2);
                }

                canvas.drawBitmap(mUpperPlatform.getBmp(), mUpperPlatform.getPos().x, mUpperPlatform.getPos().y, null);

                if (mUpperPlatform.isItemValid(0))
                    canvas.drawBitmap(
                            mUpperPlatform.getItemBmp(0),
                            mUpperPlatform.getPos().x + mUpperPlatform.getItemXpos(0),
                            mUpperPlatform.getPos().y - mUpperPlatform.getItemHeight(0),
                            null);

                if (mUpperPlatform.isItemValid(1))
                    canvas.drawBitmap(
                            mUpperPlatform.getItemBmp(1),
                            mUpperPlatform.getPos().x + mUpperPlatform.getItemXpos(1),
                            mUpperPlatform.getPos().y - mUpperPlatform.getItemHeight(1),
                            null);

            }

            if (mBottomPlatform != null) {
                if (mBottomPlatform.getPos().x <= (-1 * mBottomPlatform.getWidth())) {
                    mBottomPlatform.setPos(getNextRandomPos(mDisplayWidth, mDisplayWidth), null);

                    // Újabb elemeket lehet rádobni
                    mBottomPlatform.dropItems(2);
                }

                canvas.drawBitmap(mBottomPlatform.getBmp(), mBottomPlatform.getPos().x, mBottomPlatform.getPos().y, null);

                if (mBottomPlatform.isItemValid(0))
                    canvas.drawBitmap(
                            mBottomPlatform.getItemBmp(0),
                            mBottomPlatform.getPos().x + mBottomPlatform.getItemXpos(0),
                            mBottomPlatform.getPos().y - mBottomPlatform.getItemHeight(0),
                            null);

                if (mBottomPlatform.isItemValid(1))
                    canvas.drawBitmap(
                            mBottomPlatform.getItemBmp(1),
                            mBottomPlatform.getPos().x + mBottomPlatform.getItemXpos(1),
                            mBottomPlatform.getPos().y - mBottomPlatform.getItemHeight(1),
                            null);
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

    private void initGame() {

        mDisplayWidth = SzelektAkos.displayWidth;
        mDisplayHeight = SzelektAkos.displayHeight;
        UPPER_PLATFORM_YPOS = (int) (0.4762 * mDisplayHeight); // Felső járda y pozíciója
        BOTTOM_PLATFORM_YPOS = (int) (0.599 * mDisplayHeight); // Alsó járda y pozíciója
        BASE_PLATFORM_YPOS = (int) (0.819 * mDisplayHeight); // Alapjárda (padló) y pozíciója
        PLATFORM_HEIGHT = (int) (0.0572 * mDisplayHeight); // Járda magassága
        JUMPMAN_HEIGHT = (int) (0.1523 * mDisplayHeight); // Ugráló figura magassága
        JUMPMAN_XPOS = (int) (0.1 * mDisplayWidth); // Ugráló figura x pozíciója

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
        // Legalsó (folyamatos) járda példányainak inicializálása és méretezése
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
