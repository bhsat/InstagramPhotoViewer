package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.SearchTagResultsAdapter;
import com.codepath.instagram.activities.SearchUserResultsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;
import com.codepath.instagram.models.InstagramUser;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 11/1/15.
 */
public class SearchTagsResultFragment extends Fragment {

    private RecyclerView rvTags;
    private MenuItem progressBar;
    List<InstagramSearchTag> tags;
    SearchTagResultsAdapter adapter;

    public static SearchTagsResultFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        SearchTagsResultFragment fragment = new SearchTagsResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        tags = new ArrayList<InstagramSearchTag>();
        adapter = new SearchTagResultsAdapter(tags);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_search_tag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rvTags = (RecyclerView) view.findViewById(R.id.rvTags);
        //rvUsers.addItemDecoration(new SimpleVerticalSpacerItemDecoration(20));
        // Set Adapter
        rvTags.setAdapter(adapter);
        rvTags.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void showProgressBar() {
        // Show progress item
        progressBar.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        progressBar.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        progressBar = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(progressBar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                showProgressBar();
                fetchTags(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchTags(String query) {
        InstagramClient client = MainApplication.getRestClient();
        client.getTags(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tags.clear();
                    tags.addAll(Utils.decodeSearchTagsFromJsonResponse(response));
                    adapter.notifyDataSetChanged();
                    hideProgressBar();
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
