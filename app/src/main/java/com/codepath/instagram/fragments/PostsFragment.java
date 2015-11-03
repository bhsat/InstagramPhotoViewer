package com.codepath.instagram.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.InstagramPostsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPostsWrapper;
import com.codepath.instagram.networking.InstagramIntentService;
import com.codepath.instagram.persistence.InstagramClientDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 10/31/15.
 */
public class PostsFragment extends Fragment {

    private RecyclerView rvPosts;
    List<InstagramPost> posts;
    InstagramPostsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    InstagramClientDatabase database;

    public static PostsFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("tab", pos);
        PostsFragment fragment = new PostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<InstagramPost>();
        // Create Adapter
        adapter = new InstagramPostsAdapter(posts);
        database = InstagramClientDatabase.getInstance(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        rvPosts.addItemDecoration(new SimpleVerticalSpacerItemDecoration(30));
        // Set Adapter
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPosts();
    }

    private void fetchPosts() {
        if (isNetworkAvailable()) {
            Intent i = new Intent(getContext(), InstagramIntentService.class);
            getActivity().startService(i);
        } else {
            Toast.makeText(getContext(), "Fetching posts from cache", Toast.LENGTH_LONG).show();
            List<InstagramPost> newPosts = database.getAllInstagramPosts();
            posts.clear();
            posts.addAll(newPosts);
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(InstagramIntentService.ACTION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(InstagramIntentService.KEY_RESULTCODE, getActivity().RESULT_CANCELED);
            if (resultCode == getActivity().RESULT_OK) {
                InstagramPostsWrapper wrapper = (InstagramPostsWrapper) intent.getSerializableExtra(
                        InstagramIntentService.KEY_RESULTVALUE);
                posts.clear();
                posts.addAll(wrapper.getPosts());
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        }
    };
}
