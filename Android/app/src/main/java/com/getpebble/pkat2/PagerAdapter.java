package com.getpebble.pkat2;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    // The number of pages
    public static final int NUM_PAGES = 3;

    public PagerAdapter(FragmentManager fragManager) {
        super(fragManager);
    }

    @Override
    public Fragment getItem(int i) {
        // Create Fragment
        Fragment fragment = new PagerFragment();

        // Set position argument
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}