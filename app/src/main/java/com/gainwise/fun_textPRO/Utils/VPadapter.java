package com.gainwise.fun_textPRO.Utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gainwise.fun_textPRO.Fragments.FragColors;
import com.gainwise.fun_textPRO.Fragments.FragFont;
import com.gainwise.fun_textPRO.Fragments.FragFunify;
import com.gainwise.fun_textPRO.Fragments.FragText;

public class VPadapter extends FragmentPagerAdapter {

    private Context context;

    public VPadapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    //this returns the fragment to the viewpager, based on which tablayout index is selected
    @Override
    public Fragment getItem(int i) {
        if (i == 0){
            return new FragText();
        }else if (i == 1){
            return new FragFont();
        }
        else if (i == 2){
            return new FragColors();
        }
        else if (i == 3){
            return new FragFunify();
        }else{
            return null;
        }
    }


    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // Generate title based on item position
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TEXT";
            case 1:
                return "FONT";
            case 2:
                return "COLOR";
            case 3:
                return "FUNIFY";
            default:
                return null;
        }
    }
}
