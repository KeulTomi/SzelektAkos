package hu.foxplan.keult.szelektakos.mainscreen;

/**
 * Created by Tomi on 2017. 03. 23..
 */

import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.SzelektAkos;

import static hu.foxplan.keult.szelektakos.mainscreen.MainActivity.currentTrouser;

public class LivingRoom extends Fragment implements View.OnClickListener {

    int durationOfHands = 0;
    int durationOfEye = 0;
    Future eyeAnimStopper;
    private ImageView akosBody;
    private ImageView akosLeftHand;
    private ImageView akosRightHand;
    private ImageView animationPlace;
    private ImageView akosEyePlace;
    private Handler viewHandler = new Handler();
    private int animationNumber;
    private AnimationDrawable handAnimation;
    private AnimationDrawable eyeAnimation = new AnimationDrawable();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.livingroom, container, false);

        animationNumber = 0;

        // Méretarány kiszámítása
        float viewScaleX;
        float viewScaleY;
        float screenScaleX = SzelektAkos.displayWidth / 1300.0f;
        float screenScaleY = SzelektAkos.displayHeight / 2558.0f;
        float akosPosX;
        float akosPosY;
        int origWidth;
        int origHeight;
        int posX;
        int posY;
        ImageView imageView;

        // Ákos alkotóelemeinek méretezése
        akosBody = (ImageView) view.findViewById(R.id.akos_body);
        akosBody.setOnClickListener(this); // Ákosra kattintás kezelése

        akosPosY = 700;
        akosPosX = 30;

        imageView = akosBody;
        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.szelektakosbody).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.szelektakosbody).getHeight();
        viewScaleX = 0.95f * SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = (int) akosPosX;
        posY = (int) akosPosY;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Pislogó szem méretezése
        akosEyePlace = (ImageView) view.findViewById(R.id.eye_animation_place);
        akosEyePlace.setBackgroundResource(R.drawable.akos_body_eye_animation);

        imageView = akosEyePlace;
        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.fe01).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.fe01).getHeight();
        // ** Itt nem kell állítani méretet, mert pont ugyanannyival kell méretezni, mint Ákos-t

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = (int) akosPosX + 435;
        posY = (int) akosPosY + 122;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);


        // Pislogó szem animációja
        eyeAnimation = (AnimationDrawable) akosEyePlace.getBackground();

        // Láb méretezése

        imageView = (ImageView) view.findViewById(R.id.akos_foot);
        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.foot).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.foot).getHeight();
        viewScaleX = 0.95f * SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = (int) akosPosX;
        posY = (int) akosPosY;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Balkéz méretezése

        akosLeftHand = (ImageView) view.findViewById(R.id.left_hand);
        imageView = akosLeftHand;
        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.lefthand).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.lefthand).getHeight();
        viewScaleX = 0.95f * SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = (int) akosPosX;
        posY = (int) akosPosY;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Jobbkéz méretezése

        akosRightHand = (ImageView) view.findViewById(R.id.right_hand);
        imageView = akosRightHand;
        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.rigthhand).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.rigthhand).getHeight();
        viewScaleX = 0.95f * SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = (int) akosPosX;
        posY = (int) akosPosY;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Nadrág méretezése

        currentTrouser = (ImageView) view.findViewById(R.id.trouser);

        imageView = currentTrouser;
        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.pants00).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.pants00).getHeight();
        viewScaleX = 0.95f * SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = (int) akosPosX;
        posY = (int) akosPosY;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Fényképező és szív animációjának placeholdere
        animationPlace = (ImageView) view.findViewById(R.id.animation_place);

        // Háttér méretezése
        imageView = (ImageView) view.findViewById(R.id.livingroom_background);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.livingroom).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.livingroom).getHeight();
        viewScaleX = SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 0;
        posY = 0;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        // Fali kép méretezése

        imageView = (ImageView) view.findViewById(R.id.thgkft_picture);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.thgkft).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.thgkft).getHeight();
        // ** Itt nem kell állítani méretet, mert pont ugyanannyival kell méretezni, mint a hátteret

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 738;
        posY = 505;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);

        return view;

    }

    @Override
    public void onResume() {

        if (SzelektAkos.getTrouserToWearRes() == 0) {
            currentTrouser.setImageResource(R.drawable.pants00);
        }
        else {
            currentTrouser.setImageResource(SzelektAkos.getTrouserToWearRes());
        }

        eyeAnimation.start();

        super.onResume();
    }

    @Override
    public void onPause() {
        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();

        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (animationNumber){
            case 0:
                animationPlace.setBackgroundResource(R.drawable.akos_body_love_animation);
                handAnimation = (AnimationDrawable) animationPlace.getBackground();
                akosLeftHand.setVisibility(View.INVISIBLE);
                akosRightHand.setVisibility(View.INVISIBLE);
                animationPlace.setVisibility(View.VISIBLE);
                handAnimation.setVisible(false, false);
                handAnimation.start();

                viewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (durationOfHands == 2){
                            akosLeftHand.setVisibility(View.VISIBLE);
                            akosRightHand.setVisibility(View.VISIBLE);
                            animationPlace.setVisibility(View.INVISIBLE);
                            animationNumber = 1;
                            durationOfHands = 0;
                            viewHandler.removeCallbacks(this);
                        }
                        else {
                            durationOfHands++;
                            viewHandler.postDelayed(this, 1000);
                        }
                    }
                });
//                akosLeftHand.setVisibility(View.VISIBLE);
//                akosRightHand.setVisibility(View.VISIBLE);
//                animationPlace.setVisibility(View.INVISIBLE);
//                viewHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mAnimationIndex == mResArrayLove.length - 1) {
//                            akosLeftHand.setVisibility(View.VISIBLE);
//                            akosRightHand.setVisibility(View.VISIBLE);
//                            animationPlace.setVisibility(View.INVISIBLE);
//                            animationNumber = 1;
//                            mAnimationIndex = 0;
//                        } else {
//                            akosLeftHand.setVisibility(View.INVISIBLE);
//                            akosRightHand.setVisibility(View.INVISIBLE);
//                            animationPlace.setVisibility(View.VISIBLE);
//                            animationPlace.setImageResource(mResArrayLove[mAnimationIndex]);
//                            mAnimationIndex++;
//                            viewHandler.postDelayed(this, animationTime.intValue());
//                        }
//                    }
//                });
                break;

            case 1:
                animationPlace.setBackgroundResource(R.drawable.akos_body_photo_animation);
                handAnimation = (AnimationDrawable) animationPlace.getBackground();
                akosLeftHand.setVisibility(View.INVISIBLE);
                akosRightHand.setVisibility(View.INVISIBLE);
                animationPlace.setVisibility(View.VISIBLE);
                handAnimation.setVisible(false, false);
                handAnimation.start();

                viewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (durationOfHands == 2){
                            akosLeftHand.setVisibility(View.VISIBLE);
                            akosRightHand.setVisibility(View.VISIBLE);
                            animationPlace.setVisibility(View.INVISIBLE);
                            animationNumber = 0;
                            durationOfHands = 0;
                            viewHandler.removeCallbacks(this);
                        }
                        else {
                            durationOfHands++;
                            viewHandler.postDelayed(this, 1000);
                        }
                    }
                });
//                viewHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mAnimationIndex == mResArrayPhoto.length - 1) {
//                            mAnimationIndex = 0;
//                            akosLeftHand.setVisibility(View.VISIBLE);
//                            akosRightHand.setVisibility(View.VISIBLE);
//                            animationPlace.setVisibility(View.INVISIBLE);
//                            animationNumber = 0;
//                        } else {
//                            akosLeftHand.setVisibility(View.INVISIBLE);
//                            akosRightHand.setVisibility(View.INVISIBLE);
//                            animationPlace.setVisibility(View.VISIBLE);
//                            animationPlace.setImageResource(mResArrayPhoto[mAnimationIndex]);
//                            mAnimationIndex++;
//                            viewHandler.postDelayed(this, animationTime.intValue());
//                        }
//                    }
//                });
                break;
        }
    }
}