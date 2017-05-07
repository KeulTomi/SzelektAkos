package hu.foxplan.keult.szelektakos.mainscreen;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Tomi on 2017. 03. 23..
 */

public class BedRoom extends Fragment {

    ImageView sleepAnimPlace;
    AnimationDrawable sleepAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(hu.foxplan.keult.szelektakos.R.layout.bedroom, container, false);

        sleepAnimPlace = (ImageView) view.findViewById(hu.foxplan.keult.szelektakos.R.id.sleeping_anim_place);
        sleepAnimPlace.setBackgroundResource(hu.foxplan.keult.szelektakos.R.drawable.bed_room_sleeping_animation);
        sleepAnimation = (AnimationDrawable) sleepAnimPlace.getBackground();

        sleepAnimation.start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
