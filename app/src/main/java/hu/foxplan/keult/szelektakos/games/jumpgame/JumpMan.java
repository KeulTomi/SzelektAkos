package hu.foxplan.keult.szelektakos.games.jumpgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;

/**
 * Bitmap objektum adatait tároló objektum
 */

public class JumpMan {

    private final int FOOT_ANIMATION_CYCLE = 25; // Lábmozgás animáció frissítési ciklusa
    private final int JUMP_REFRESH_CYCLE = 10; // Az ugró ember animációjának frissítési ciklusa
    private final int GRAVITY_STEP = 20; // Egy frissítési ciklusban hány pixelt emelkedjen vagy csökkenjen

    private Handler viewHandler;
    private Context mContext;
    private Point pos = new Point(0, 0);
    private int mCurrrentBmpIndex;
    private Integer[] mResourceArray;
    private Bitmap[] mBitmapArray;
    private int posUpperLimit;
    private int posBottomLimit;
    private boolean isGravityOn;
    private boolean isJumping;
    private int v0; // Ugrás induló sebessége
    private int jumpCycleCount; // Ugrás mozzanatok számlálója (hanyadik mozzanat)
    private int y0; // Ugrás induló y pozíciója
    private int y1; // Ugrás menet közbeni pozíciója

    JumpMan(Integer[] resArray, int targetHeight, Handler handler, Context context) {
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
        isJumping = false;
    }

    void startAnimation() {

        // Lábmozgás animációja
        viewHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrrentBmpIndex == mResourceArray.length - 1) {
                    mCurrrentBmpIndex = 0;
                } else
                    mCurrrentBmpIndex++;

                viewHandler.postDelayed(this, FOOT_ANIMATION_CYCLE);
            }
        });

        // Gravitáció indítása
        viewHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isGravityOn) {
                    if (posBottomLimit > (getPos().y))
                        setPos(null, getPos().y + GRAVITY_STEP);
                    else
                        setPos(null, posBottomLimit); // Nem zuhan tovább, beállítás pontosan a limitre
                }
                viewHandler.postDelayed(this, JUMP_REFRESH_CYCLE);
            }
        });
    }

    void setPos(Integer x, Integer y) {
        if (x != null)
            this.pos.x = x;
        if (y != null)
            this.pos.y = y;
    }

    Point getPos() {
        return pos;
    }

    Bitmap getBmp() {
        return mBitmapArray[mCurrrentBmpIndex];
    }

    int getHeight() {
        return mBitmapArray[mCurrrentBmpIndex].getHeight();
    }

    int getWidth() {
        return mBitmapArray[mCurrrentBmpIndex].getWidth();
    }

    void jump() {

        if (isJumping)
            return; // Ha már ugrásban van akkor nem indítható még egy ugrás

        v0 = 40; // Kezdő sebesség beállítása
        jumpCycleCount = 1; // Idő inicializlása (első "másodperc")
        y0 = getPos().y; // Ugrás induló pozíciójának tárolása

        // Ugrás animációja
        if (getPos().y == posBottomLimit) {
            isJumping = true; // Ismételt ugrás kizárása
                viewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        isGravityOn = false;
                        y1 = v0 * jumpCycleCount - 1 * jumpCycleCount * jumpCycleCount;
                        if ((getPos().y <= posBottomLimit) && (getPos().y >= posUpperLimit)) {
                            setPos(null, y0 - y1);
                            jumpCycleCount++; // idő növelése
                            viewHandler.postDelayed(this, JUMP_REFRESH_CYCLE);
                        } else {
                            isGravityOn = true;
                            isJumping = false;
                        }

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
