package hu.foxplan.keult.szelektakos.games.jumpgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;

import hu.foxplan.keult.szelektakos.SzelektAkos;

/**
 * Bitmap objektum adatait tároló objektum
 */

class JumpMan {

    private final int FOOT_ANIMATION_CYCLE = 25; // Lábmozgás animáció frissítési ciklusa
    private final int JUMP_REFRESH_CYCLE = 10; // Az ugró ember animációjának frissítési ciklusa
    private final int GRAVITY_STEP = 20; // Egy frissítési ciklusban hány pixelt emelkedjen vagy csökkenjen
    public boolean isFalling;
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
    private float gravityFactor; // Gravitációs tényező ugrás lassításához
    private int jumpCycleCount; // Ugrás mozzanatok számlálója (hanyadik mozzanat)
    private int y0; // Ugrás induló y pozíciója
    private int dy; // Ugrás menet közbeni pozíciója
    private int yLast;

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
                    (int) (bmp.getWidth() * targetHeight / (float) bmp.getHeight()),
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
        v0 = 40 * SzelektAkos.displayHeight / 960; // Kezdő sebesség beállítása
        gravityFactor = 0.7f * SzelektAkos.displayHeight / 960;
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

                if (GameThread.isGameRunning)
                    viewHandler.postDelayed(this, FOOT_ANIMATION_CYCLE);
            }
        });

        yLast = getPos().y;

        // Gravitáció indítása
        viewHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isGravityOn) {

                    if (yLast == posBottomLimit)
                        isFalling = false;

                    if (posBottomLimit > (getPos().y)) {
                        setPos(null, getPos().y + GRAVITY_STEP);
                        isFalling = true;
                    } else {
                        setPos(null, posBottomLimit); // Nem zuhan tovább, beállítás pontosan a limitre
                    }

                    yLast = getPos().y;
                }
                if (GameThread.isGameRunning)
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

    void jump(boolean jumpFromBluePlatform) {

        if (isJumping)
            return; // Ha már ugrásban van akkor nem indítható még egy ugrás

        jumpCycleCount = 1; // Idő inicializlása (első "másodperc")
        y0 = getPos().y; // Ugrás induló pozíciójának tárolása

        if (jumpFromBluePlatform)
            v0 = 30 * SzelektAkos.displayHeight / 960; // Kezdő sebesség beállítása
        else
            v0 = 35 * SzelektAkos.displayHeight / 960; // Kezdő sebesség beállítása

        // Ugrás animációja
        if (getPos().y == posBottomLimit) {
            isJumping = true; // Ismételt ugrás kizárása
                viewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        isGravityOn = false;
                        dy = v0 * jumpCycleCount - (int) (gravityFactor * jumpCycleCount * jumpCycleCount);

                        isFalling = yLast < (y0 - dy);

                        if ((getPos().y <= posBottomLimit) && (getPos().y >= posUpperLimit)) {
                            setPos(null, y0 - dy);
                            jumpCycleCount++; // idő növelése
                            yLast = getPos().y;
                            if (GameThread.isGameRunning)
                                viewHandler.postDelayed(this, JUMP_REFRESH_CYCLE);
                        } else {
                            isGravityOn = true;
                            isJumping = false;
                        }

                    }
                });
        }
    }

    void setPosBottomLimit(int posBottomLimit) {
        this.posBottomLimit = posBottomLimit;
    }

    void setPosUpperLimit(int posUpperLimit) {
        this.posUpperLimit = posUpperLimit;
    }
}
