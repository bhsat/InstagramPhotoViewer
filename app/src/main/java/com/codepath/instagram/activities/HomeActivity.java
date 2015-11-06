package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PostsFragment;
import com.codepath.instagram.fragments.ProfileFragment;
import com.codepath.instagram.fragments.SearchFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_home);
        fragments = new ArrayList<Fragment>();

        // Fragment <code></code>

        // Create new instance
        /*FragmentManager fm = getSupportFragmentManager();

        // get instance of fragment transaction
        FragmentTransaction ft = fm.beginTransaction();

        // create a fragment
        PostsFragment postsFragment = new PostsFragment();

        // add it to placeholder - framelayout
        ft.replace(R.id.flPostsContainer, postsFragment);
        ft.addToBackStack(null);
        // commit transaction
        ft.commit();*/

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragments.add(0, new PostsFragment());
        fragments.add(1, new SearchFragment());
        fragments.add(2, new PostsFragment());
        fragments.add(3, new PostsFragment());
        fragments.add(4, new ProfileFragment());
        HomeFragmentStatePagerAdapter adapter = new HomeFragmentStatePagerAdapter(getSupportFragmentManager(),
                HomeActivity.this, fragments);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tlSlide);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            //rvPosts.smoothScrollToPosition(0);
            return true;
        } /*else if (id == R.id.tvUser2) {
            //rvPosts.smoothScrollToPosition(rvPosts.getAdapter().getItemCount());
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


}
