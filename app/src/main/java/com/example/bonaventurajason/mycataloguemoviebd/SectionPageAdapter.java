package com.example.bonaventurajason.mycataloguemoviebd;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SectionPageAdapter extends FragmentStatePagerAdapter {
    int mNumTab;

    public SectionPageAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumTab = mNumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NowShowing();
            case 1:
                return new ComingSoon();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumTab;
    }
}
