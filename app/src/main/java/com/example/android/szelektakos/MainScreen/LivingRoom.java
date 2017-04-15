package com.example.android.szelektakos.MainScreen;

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

public class LivingRoom extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.livingroom, container, false);

        MainActivity.currentTrouser = (ImageView) view.findViewById(R.id.trouser);

        return view;
    }

    @Override
    public void onResume() {

        if (SzelektAkos.getTrouserToWearRes() == 0) {
            MainActivity.currentTrouser.setImageResource(R.mipmap.pants00);
        }
        else {
            MainActivity.currentTrouser.setImageResource(SzelektAkos.getTrouserToWearRes());
        }
        super.onResume();
    }

}