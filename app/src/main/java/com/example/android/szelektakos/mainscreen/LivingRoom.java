package com.example.android.szelektakos.mainscreen;

/**
 * Created by Tomi on 2017. 03. 23..
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.szelektakos.R;
import com.example.android.szelektakos.SzelektAkos;

import static com.example.android.szelektakos.mainscreen.MainActivity.currentTrouser;

public class LivingRoom extends Fragment implements View.OnClickListener {

    private ImageView akosBody;
    private ImageView akosLeftHand;
    private ImageView akosRightHand;
    private ImageView loveAnimationPlace;
    private int animationNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.livingroom, container, false);

        currentTrouser = (ImageView) view.findViewById(R.id.trouser);
        akosBody = (ImageView) view.findViewById(R.id.akos_body);
        akosLeftHand = (ImageView) view.findViewById(R.id.left_hand);
        akosRightHand = (ImageView) view.findViewById(R.id.right_hand);

        loveAnimationPlace = (ImageView) view.findViewById(R.id.love_animation_place);
//        loveAnimationPlace.setBackgroundResource(R.drawable.akos_body_love_animation);
//
//        animationNumber = 0;
//        akosBody.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {

        if (SzelektAkos.getTrouserToWearRes() == 0) {
            currentTrouser.setImageResource(R.mipmap.pants_thg);
        }
        else {
            currentTrouser.setImageResource(SzelektAkos.getTrouserToWearRes());
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (animationNumber){
            case 0:
//                akosRightHand.setVisibility(View.GONE);
//                akosLeftHand.setVisibility(View.GONE);
//                loveAnimationPlace.setVisibility(View.VISIBLE);
//                AnimationDrawable loveAnimation = (AnimationDrawable) loveAnimationPlace.getBackground();
//                loveAnimation.start();
//
//                while (loveAnimation.isRunning() == false){
//
//                }
//
//                akosRightHand.setVisibility(View.VISIBLE);
//                akosLeftHand.setVisibility(View.VISIBLE);
//                loveAnimationPlace.setVisibility(View.GONE);
        }
    }
}