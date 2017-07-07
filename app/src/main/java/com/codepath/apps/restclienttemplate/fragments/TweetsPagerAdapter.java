package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by fmonsalve on 7/7/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = {"Home", "Mentions"};
    private Fragment[] fragments = new Fragment[2];
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    //return the total num of fragments
    @Override
    public int getCount() {
        return 2;
    }

    //return the fragment to use depending on the position
    @Override
    public Fragment getItem(int position) {
        if (position==0) {
            fragments[0]=new HomeTimelineFragment();
            return fragments[0];
        }else if (position==1) {
            fragments[1]=new MentionsTimelineFragment();
            return fragments[1];
        }else
            return null;
    }

    //return title
    @Override
    public CharSequence getPageTitle(int position) {
        //return title based on item position
        return tabTitles[position];
    }

    public Fragment getFragmentInstance(int position){
        return fragments[position];
    }
}
