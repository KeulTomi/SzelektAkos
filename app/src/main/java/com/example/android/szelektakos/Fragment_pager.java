package com.example.android.szelektakos;

/**
 * Created by Tomi on 2017. 03. 23..
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class Fragment_Pager extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    public Fragment_Pager(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new BedRoom();
            case 1:
                // Calling a Fragment without sending arguments
                return new LivingRoom();
            case 2:
                return new Kitchen();
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
