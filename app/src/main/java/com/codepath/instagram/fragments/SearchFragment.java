package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.activities.HomeFragmentStatePagerAdapter;
import com.codepath.instagram.activities.SearchFragmentStatePagerAdapter;

/**
 * Created by bhavanis on 11/1/15.
 */
public class SearchFragment extends Fragment {

    public static SearchFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("tab", pos);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vpSearch);
        SearchFragmentStatePagerAdapter adapter = new SearchFragmentStatePagerAdapter(getFragmentManager(),
                view.getContext());
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tlSearch);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void insertFirstFragment() {
        Fragment firstChildFragment = new SearchUsersResultFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flSearchContainer, firstChildFragment).commit();
    }

    private void insertSecondFragment() {
        Fragment secondChildFragment = new SearchTagsResultFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flSearchContainer, secondChildFragment).commit();
    }
}
