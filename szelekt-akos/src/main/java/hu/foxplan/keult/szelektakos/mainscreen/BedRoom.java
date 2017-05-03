package hu.foxplan.keult.szelektakos.mainscreen;

import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import hu.foxplan.keult.szelektakos.R;
import hu.foxplan.keult.szelektakos.SzelektAkos;

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

        scaleImageItems(view);

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

    private void scaleImageItems(View layout) {

        // Méretarány kiszámítása
        float viewScaleX;
        float viewScaleY;
        float screenScaleX = SzelektAkos.displayWidth / 1300.0f;
        float screenScaleY = SzelektAkos.displayHeight / 2558.0f;
        float akosPosY;
        int origWidth;
        int origHeight;
        int posX;
        int posY;
        ImageView imageView;

        // Menü háttér átméretezése
        imageView = (ImageView) layout.findViewById(R.id.bed_room_background);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.bedroom).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.bedroom).getHeight();
        viewScaleX = SzelektAkos.displayWidth / (float) origWidth;
        viewScaleY = SzelektAkos.displayHeight / (float) origHeight;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 0;
        posY = 155;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);


        //  pozícionálása és méretezése
        imageView = (ImageView) layout.findViewById(R.id.sleeping_anim_place);

        origWidth = BitmapFactory.decodeResource(getResources(), R.drawable.zzz00).getWidth();
        origHeight = BitmapFactory.decodeResource(getResources(), R.drawable.zzz00).getHeight();
        viewScaleX = 0.23f * SzelektAkos.displayWidth / (float) origWidth;

        imageView.getLayoutParams().width = (int) (origWidth * viewScaleX);
        imageView.getLayoutParams().height = (int) (origHeight * viewScaleX);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        posX = 837;
        posY = 1213;
        imageView.setX(posX * screenScaleX);
        imageView.setY(posY * screenScaleY);
    }
}
