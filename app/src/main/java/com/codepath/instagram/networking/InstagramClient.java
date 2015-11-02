package com.codepath.instagram.networking;

import android.content.Context;

import com.codepath.instagram.helpers.Constants;
import com.codepath.instagram.helpers.Utils;
import com.codepath.oauth.OAuthAsyncHttpClient;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * Created by bhavanis on 10/28/15.
 */
public class InstagramClient extends OAuthBaseClient {


    private static final String feedUrl = "https://api.instagram.com/v1/users/self/feed";
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
        client.get(feedUrl, responseHandler);
    }

    public void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
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
