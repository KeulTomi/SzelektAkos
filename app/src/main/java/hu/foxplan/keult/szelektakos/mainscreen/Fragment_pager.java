package hu.foxplan.keult.szelektakos.mainscreen;

/**
 * Created by Tomi on 2017. 03. 23..
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Fragment_pager extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;

    public Fragment_pager(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                // Hálószobára lapozott a felhasználó
                return new BedRoom();
            case 1:
                // Nappalira lapozott a felhasználó
                return new LivingRoom();
            case 2:
                // Konyhára lapozott a felhasználó
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
