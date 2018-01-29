package com.example.devomac02.faunanimal_android;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MiFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;


    public MiFragmentPagerAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
        super(supportFragmentManager);
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        //controlar que pestaña es pulsada y cargar el fragment correspondiente
        switch (position) {
            case 0:
                f = fragment_home.newInstance();
                break;
            case 1:
                f = fragment_search.newInstance();
                break;
            case 2:
                f = fragment_cart.newInstance();
                break;
            case 3:
                f = fragment_info.newInstance();
                break;
        }

        return f;
    }

}