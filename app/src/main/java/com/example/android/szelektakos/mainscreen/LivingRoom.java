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

import static com.example.android.szelektakos.mainscreen.MainActivity.*;

public class LivingRoom extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.livingroom, container, false);

        currentTrouser = (ImageView) view.findViewById(R.id.trouser);

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

}