package com.codepath.instagram.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    List<InstagramComment> comments;
    private RecyclerView rvComments;
    InstagramClient client;
    InstagramCommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        comments = new ArrayList<InstagramComment>();

        getSupportActionBar().setTitle("Comments");
        String mediaId = getIntent().getStringExtra("mediaId");

        rvComments = (RecyclerView) findViewById(R.id.rvComments);
        //rvComments.addItemDecoration(new SimpleVerticalSpacerItemDecoration(30));

        // Create Adapter
        adapter = new InstagramCommentsAdapter(comments);

        // Set Adapter
        rvComments.setAdapter(adapter);

        // Set layout
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        client = new InstagramClient(this);
        client.getComments(mediaId, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            comments.clear();
            comments.addAll(Utils.decodeCommentsFromJsonResponse(response));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject obj) {
            Toast.makeText(getBaseContext(), "FAILED to get comments", Toast.LENGTH_LONG).show();
        }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
