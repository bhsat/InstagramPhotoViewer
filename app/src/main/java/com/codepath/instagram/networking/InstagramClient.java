package com.codepath.instagram.networking;

import android.content.Context;
import android.util.Log;

import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.scribe.builder.api.Api;

/**
 * Created by bhavanis on 10/28/15.
 */
public class InstagramClient extends OAuthBaseClient {

    private AsyncHttpClient syncClient = new SyncHttpClient();

    private static final String apiUrl = "https://api.instagram.com/v1/media/popular?client_id=";
    private static final String feedUrl = "https://api.instagram.com/v1/users/self/feed";
    private static final String profileUrl = "https://api.instagram.com/v1/users/self";
    private static final String imageUrl = "https://api.instagram.com/v1/users/self/media/recent/";
    public static final Class<? extends Api> REST_API_CLASS = InstagramApi.class;
    public static final String REST_URL = "https://api.instagram.com/v1/";
    public static final String REST_CONSUMER_KEY = "";
    public static final String REST_CONSUMER_SECRET = "";
    public static final String REST_URI = Constants.REDIRECT_URI;
    public static final String SCOPE = Constants.SCOPE;

    public InstagramClient(Context c) {
        super(c, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_URI, SCOPE);
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        Log.i("SyncClient token", client.getAccessToken().getToken());
        syncClient.get(feedUrl, params, responseHandler);

    }

    public void getUserProfile(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        client.get(profileUrl, params, responseHandler);
    }

    public void getUserMedia(JsonHttpResponseHandler responseHandler) {
        client.get(imageUrl, responseHandler);
    }

    public void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        //AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/"+mediaId+"/comments?client_id=";
        client.get(url, responseHandler);
    }

    public void getUsers(String query, JsonHttpResponseHandler responseHandler) {
        String url = "https://api.instagram.com/v1/users/search?q="+query;
        client.get(url, responseHandler);
    }

    public void getTags(String query, JsonHttpResponseHandler responseHandler) {
        String url = "https://api.instagram.com/v1/tags/search?q="+query;
        client.get(url, responseHandler);
    }
}
