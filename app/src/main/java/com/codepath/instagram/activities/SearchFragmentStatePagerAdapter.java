package com.codepath.instagram.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.fragments.SearchFragment;
import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

/**
 * Created by bhavanis on 11/1/15.
 */
public class SearchFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {

    private static int TAB_COUNT = 2;
    private Context context;
    private static String[] titles = {"USERS", "TAGS"};

    public SearchFragmentStatePagerAdapter(FragmentManager fragmentManager, Context ctx) {
        super(fragmentManager);
        this.context = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchUsersResultFragment.newInstance(position);
            case 1:
                return SearchTagsResultFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
