package com.codepath.instagram.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PostsFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by bhavanis on 11/1/15.
 */
public class HomeFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {

    private List<Fragment> fragments;
    final int TAB_COUNT = 5;
    private Context context;
    private int[] imageResId = {
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_capture,
            R.drawable.ic_notifs,
            R.drawable.ic_profile
    };

    public HomeFragmentStatePagerAdapter(FragmentManager fragmentManager, Context context, List<Fragment> fragmentsList) {
        super(fragmentManager);
        this.context = context;
        this.fragments = fragmentsList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
        /*switch (positi) {
            case 0: // Fragment # 0 - This will show PostsFragment
                return PostsFragment.newInstance(position +1);
            case 1: // Fragment # 1 - This will show SearchUsersResultFragment
                return SearchUsersResultFragment.newInstance(position+1);
            default:
                return null;
        }*/
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
