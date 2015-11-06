package com.codepath.instagram.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.InstagramPostsAdapter;
import com.codepath.instagram.activities.PhotosAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramImage;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPostsWrapper;
import com.codepath.instagram.models.InstagramProfile;
import com.codepath.instagram.models.InstagramUser;
import com.codepath.instagram.networking.InstagramClient;
import com.codepath.instagram.networking.InstagramIntentService;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 10/31/15.
 */
public class ProfileFragment extends Fragment {

    PhotosAdapter photosAdapter;
    SimpleDraweeView ivRoundProfile;
    public TextView tvPostsCount;
    public TextView tvFollowers;
    public TextView tvFollowing;
    ForegroundColorSpan grayColorSpan;
    ForegroundColorSpan blackColorSpan;
    RecyclerView rvPhotos;
    List<InstagramImage> images;


    InstagramClientDatabase database;

    public static ProfileFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("tab", pos);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivRoundProfile = (SimpleDraweeView) view.findViewById(R.id.ivRoundProfile);
        tvPostsCount = (TextView) view.findViewById(R.id.tvPostCount);
        tvFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        rvPhotos = (RecyclerView) view.findViewById(R.id.rvPhotos);
        grayColorSpan = new ForegroundColorSpan(getContext().getResources().getColor(R.color.gray_text));
        blackColorSpan = new ForegroundColorSpan(getContext().getResources().getColor(R.color.black));
        photosAdapter = new PhotosAdapter(images);
        // Set Adapter
        rvPhotos.setAdapter(photosAdapter);
        rvPhotos.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvPhotos.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();
        // Create Adapter
        database = InstagramClientDatabase.getInstance(getContext());

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchProfile();
        fetchImages();

    }

    private void fetchProfile() {
        InstagramClient client = MainApplication.getRestClient();
        client.getUserProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                InstagramProfile user = InstagramProfile.fromJson(response);
                //Log.i("Tags:", user.profilePictureurl +" "+ user.media);
                //ivRoundProfile.setImageURI(Uri.parse(user.profilePictureurl));
                tvPostsCount.setText(setSpan(String.valueOf(user.media), "posts"));
                tvFollowers.setText(setSpan(String.valueOf(user.followedBy), "follows"));
                tvFollowing.setText(setSpan(String.valueOf(user.follows), "following"));
                ivRoundProfile.setImageURI(Uri.parse(user.profilePictureurl));
                /*PhotoGridFragment secFrag = PhotoGridFragment.newInstance(0);
                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.flPhotosContainer, secFrag);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject obj) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
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

    private SpannableStringBuilder setSpan(String firstWord, String secondWord) {
        // Use a SpannableStringBuilder so that both the text and the spans are mutable
        SpannableStringBuilder ssb = new SpannableStringBuilder(firstWord);
        TypefaceSpan typeSpan = new TypefaceSpan("sans-serif-medium");

        ssb.setSpan(
                blackColorSpan,
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(typeSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(Typeface.BOLD, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Add a blank space
        ssb.append("\n");

        // Add the caption and apply the font settings to only caption
        if (secondWord != null) {
            ssb.append(secondWord);
            ssb.setSpan(
                    grayColorSpan,
                    ssb.length() - secondWord.length(),
                    ssb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            TypefaceSpan typeSpanCaption = new TypefaceSpan("sans-serif");
            ssb.setSpan(typeSpanCaption, ssb.length() - secondWord.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

}
