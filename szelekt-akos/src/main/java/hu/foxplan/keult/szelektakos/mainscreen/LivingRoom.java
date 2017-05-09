package hu.foxplan.keult.szelektakos.mainscreen;

/**
 * Created by Tomi on 2017. 03. 23..
 */

import android.content.Intent;
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
import hu.foxplan.keult.szelektakos.THG_Web;

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
    private ImageView THGPicture;
    private Handler viewHandler = new Handler();
    private int animationNumber;
    private AnimationDrawable handAnimation;
    private AnimationDrawable eyeAnimation = new AnimationDrawable();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.livingroom, container, false);

        animationNumber = 0;

        akosBody = (ImageView) view.findViewById(R.id.akos_body);
        akosBody.setOnClickListener(this); // Ákosra kattintás kezelése

        akosEyePlace = (ImageView) view.findViewById(R.id.eye_animation_place);
        akosEyePlace.setBackgroundResource(R.drawable.akos_body_eye_animation);

        akosRightHand = (ImageView) view.findViewById(R.id.right_hand);
        akosLeftHand = (ImageView) view.findViewById(R.id.left_hand);

        currentTrouser = (ImageView) view.findViewById(R.id.trouser);

        THGPicture = (ImageView) view.findViewById(R.id.thgkft_picture);
        THGPicture.setOnClickListener(this);

        // Fényképező és szív animációjának placeholdere
        animationPlace = (ImageView) view.findViewById(R.id.animation_place);

        // Pislogó szem animációja
        eyeAnimation = (AnimationDrawable) akosEyePlace.getBackground();

        //scaleImageItems(view);

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
        if (view.getId() == R.id.thgkft_picture) {
            Intent openTHGWEB = new Intent(getContext(), THG_Web.class);
            startActivity(openTHGWEB);
            return;
        }
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