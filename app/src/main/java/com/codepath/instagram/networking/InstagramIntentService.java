package com.codepath.instagram.networking;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPostsWrapper;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 11/3/15.
 */
public class InstagramIntentService extends IntentService {
    public static final String ACTION = "com.codepath.instagram.InstagramIntentService";
    public static final String KEY_RESULTCODE = "resultcode";
    public static final String KEY_RESULTVALUE = "resultvalue";
    InstagramClientDatabase database;
    List<InstagramPost> posts;

    public InstagramIntentService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        database = InstagramClientDatabase.getInstance(getApplicationContext());
        posts = new ArrayList<InstagramPost>();
        fetchPostsFromApi();
        InstagramPostsWrapper wrapper = new InstagramPostsWrapper(posts);

        Intent in = new Intent(ACTION);
        in.putExtra(KEY_RESULTCODE, Activity.RESULT_OK);
        in.putExtra(KEY_RESULTVALUE, wrapper);

        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }

    private void fetchPostsFromApi() {
        InstagramClient client = MainApplication.getRestClient();
        client.getPopularFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                posts.clear();
                posts.addAll(Utils.decodePostsFromJsonResponse(response));
                database.emptyAllTables();
                database.addInstagramPosts(posts);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject obj) {
            }
        });
    }
}
