package com.example.android.szelektakos.games.jumpgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;

import com.example.android.szelektakos.R;

import java.util.Random;

/**
 * Bitmap objektum adatait tároló objektum
 */

public class Platform implements Runnable {

    private static Item[] sourceItems;
    private final int PLATFORM_MOVE_CYCLE = 10; // Járda pozíció frissítési ciklusa
    private final int PLATFORM_MOVE_STEP = 5; // Egy frissítési ciklusban hány pixelt lépjen a járda
    private Point pos = new Point(0, 0);
    private Bitmap bmp;
    private Item[] carriedItems;
    private Handler viewHandler;

    public Platform(Bitmap bmp, int targetHeight, Handler handler) {
        this.bmp = Bitmap.createScaledBitmap(
                bmp,
                (int) (bmp.getWidth() * targetHeight / bmp.getHeight()),
                (int) targetHeight,
                false);

        if (handler != null) {
            this.viewHandler = handler;
        }
    }

    public static void loadItemResources(Context context) {

        sourceItems = new Item[6];

        sourceItems[0] = new Item();
        sourceItems[0].bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_baditem_01);
        sourceItems[0].isBadItem = true;

        sourceItems[1] = new Item();
        sourceItems[1].bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_baditem_02);
        sourceItems[1].isBadItem = true;

        sourceItems[2] = new Item();
        sourceItems[2].bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_baditem_03);
        sourceItems[2].isBadItem = true;

        sourceItems[3] = new Item();
        sourceItems[3].bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_gooditem_01);
        sourceItems[3].isBadItem = false;

        sourceItems[4] = new Item();
        sourceItems[4].bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_gooditem_02);
        sourceItems[4].isBadItem = false;

        sourceItems[5] = new Item();
        sourceItems[5].bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_gooditem_03);
        sourceItems[5].isBadItem = false;

    }

    public void startAnimation() {
        viewHandler.postDelayed(this, PLATFORM_MOVE_CYCLE);
    }

    public void dropItems(int itemsToDrop) {
        Random selector = new Random();
        carriedItems = new Item[itemsToDrop];
        int randomPos;

        for (int i = 0; i < itemsToDrop; i++) {
            randomPos = selector.nextInt(30);
            carriedItems[i] = new Item();

            if (randomPos < sourceItems.length) {
                carriedItems[i].bmp = sourceItems[randomPos].bmp;
                carriedItems[i].isBadItem = sourceItems[randomPos].isBadItem;
                carriedItems[i].relativePos = carriedItems[i].bmp.getWidth() + selector.nextInt(getWidth() - 2 * carriedItems[i].bmp.getWidth());
            }
        }

    }

    public Bitmap getItemBmp(int num) {
        return carriedItems[num].bmp;
    }

    public int getItemXpos(int num) {
        return carriedItems[num].relativePos;
    }

    public boolean isItemValid(int num) {
        if (carriedItems[num].bmp != null)
            return true;
        else
            return false;
    }

    public void invalidateItem(int num) {
        carriedItems[num].bmp = null;
    }

    public int getItemHeight(int num) {
        return carriedItems[num].bmp.getHeight();
    }

    public boolean isItemBad(int num) {
        return carriedItems[num].isBadItem;
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
        return bmp;
    }

    public int getHeight() {
        return bmp.getHeight();
    }

    public int getWidth() {
        return bmp.getWidth();
    }

    @Override
    public void run() {
        setPos(getPos().x - PLATFORM_MOVE_STEP, getPos().y);
        viewHandler.postDelayed(this, PLATFORM_MOVE_CYCLE);
    }

    public static class Item {
        Bitmap bmp;
        boolean isBadItem;
        int relativePos;
    }
}
