
package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotosAdapter;
import com.codepath.instagram.activities.SearchFragmentStatePagerAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramImage;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramUser;
import com.codepath.instagram.networking.InstagramClient;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PhotoGridFragment extends Fragment {

    List<InstagramImage> images;
    private RecyclerView rvPhotos;
    PhotosAdapter photosAdapter;

    public static PhotoGridFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("tab", pos);
        PhotoGridFragment fragment = new PhotoGridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<InstagramImage>();
        // Create Adapter

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        rvPhotos = (RecyclerView) view.findViewById(R.id.rvPhotos);
        photosAdapter = new PhotosAdapter(images);
        // Set Adapter
        rvPhotos.setAdapter(photosAdapter);
        rvPhotos.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvPhotos.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchImages();
    }

    private void fetchImages() {
        InstagramClient client = MainApplication.getRestClient();
        client.getUserMedia(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                images.clear();
                List<InstagramPost> posts = Utils.decodePostsFromJsonResponse(response);
                for (InstagramPost post : posts) {
                    images.add(post.image);
                }
                photosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject obj) {
                Toast.makeText(getContext(), "Failed to get images", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

