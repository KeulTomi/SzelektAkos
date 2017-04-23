package com.example.android.szelektakos.games.jumpgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;

/**
 * Bitmap objektum adatait tároló objektum
 */

public class JumpMan {

    private final int JUMPMAN_ANIMATION_CYCLE = 25; // Az ugró ember animációjának frissítési ciklusa
    private final int JUMPMAN_JUMP_SPEED = 10; // Az ugró ember animációjának frissítési ciklusa
    private final int JUMPMAN_ELEVATION_STEP = 20; // Egy frissítési ciklusban hány pixelt emelkedjen vagy csökkenjen
    private Handler viewHandler;
    private Context mContext;
    private Point pos = new Point(0, 0);
    private int mCurrrentBmpIndex;
    private Integer[] mResourceArray;
    private Bitmap[] mBitmapArray;
    private int posUpperLimit;
    private int posBottomLimit;
    private boolean isGravityOn;

    public JumpMan(Integer[] resArray, int targetHeight, Handler handler, Context context) {
        // Kapott paraméterek mentése
        this.mContext = context;
        this.mResourceArray = new Integer[resArray.length];
        this.mBitmapArray = new Bitmap[resArray.length];

        Bitmap bmp;
        for (int i = 0; i < resArray.length; i++) {
            bmp = BitmapFactory.decodeResource(mContext.getResources(), resArray[i]);
            mBitmapArray[i] = Bitmap.createScaledBitmap(
                    bmp,
                    (int) (bmp.getWidth() * targetHeight / bmp.getHeight()),
                    targetHeight,
                    false);
        }

        System.arraycopy(resArray, 0, this.mResourceArray, 0, resArray.length);
        mCurrrentBmpIndex = 0;

        if (handler != null) {
            this.viewHandler = handler;
        }

        isGravityOn = true;
    }


    public void startAnimation() {

        // Lábmozgás animációja
        viewHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrrentBmpIndex == mResourceArray.length - 1) {
                    mCurrrentBmpIndex = 0;
                } else
                    mCurrrentBmpIndex++;

                viewHandler.postDelayed(this, JUMPMAN_ANIMATION_CYCLE);
            }
        });

        // Gravitáció indítása
        viewHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isGravityOn) {
                    if (posBottomLimit > (getPos().y))
                        setPos(null, getPos().y + JUMPMAN_ELEVATION_STEP);
                    else
                        setPos(null, posBottomLimit); // Nem zuhan tovább, beállítás pontosan a limitre
                }
                viewHandler.postDelayed(this, JUMPMAN_JUMP_SPEED);
            }
        });
    }

    public void setPos(Integer x, Integer y) {
        if (x != null)
            this.pos.x = x;
        if (y != null)
            this.pos.y = y;
    }

    public Point getPos() {
        return pos;
    }

    public Bitmap getBmp() {
        return mBitmapArray[mCurrrentBmpIndex];
    }

    public int getHeight() {
        return mBitmapArray[mCurrrentBmpIndex].getHeight();
    }

    public int getWidth() {
        return mBitmapArray[mCurrrentBmpIndex].getWidth();
    }

    public void jump() {

        // Ugrás animációja
        if (getPos().y == posBottomLimit) {
            viewHandler.post(new Runnable() {
                @Override
                public void run() {

                    isGravityOn = false;
                    if (posUpperLimit <= (getPos().y)) {
                        setPos(null, getPos().y - JUMPMAN_ELEVATION_STEP);
                        viewHandler.postDelayed(this, JUMPMAN_JUMP_SPEED);
                    } else
                        isGravityOn = true;

                }
            });
        }
    }

    public void setPosBottomLimit(int posBottomLimit) {
        this.posBottomLimit = posBottomLimit;
    }

    public void setPosUpperLimit(int posUpperLimit) {
        this.posUpperLimit = posUpperLimit;
    }
}
