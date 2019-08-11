package com.sr7d.mitplacement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    int flag;
    public ViewPagerAdapter(FragmentManager fm, int flag) {
        super(fm);
        this.flag = flag;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i)
        {
            case 0:
                if (flag == 1) {
                    fragment = new AdminFragment();
                } else {
                    fragment = new DashboardFragment();
                }
                break;
            case 1:
                fragment = new UpcomingFragment();
                break;
            case 2:
                fragment = new ProgressFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Dashboard";
        }
        else if (position == 1)
        {
            title = "Upcoming";
        }
        else if (position == 2)
        {
            title = "Progress";
        }
        return title;
    }
}
