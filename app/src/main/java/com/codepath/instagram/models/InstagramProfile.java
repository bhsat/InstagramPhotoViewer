package com.codepath.instagram.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 11/5/15.
 */
public class InstagramProfile implements Serializable {
    public String userName;
    public String profilePictureurl;
    public int media;
    public int followedBy;
    public int follows;

    public static InstagramProfile fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        InstagramProfile profile = new InstagramProfile();

        try {
            JSONObject data = jsonObject.getJSONObject("data");
            profile.userName = data.getString("username");
            profile.profilePictureurl = data.getString("profile_picture");
            JSONObject counts = data.getJSONObject("counts");
            profile.media = counts.getInt("media");
            profile.followedBy = counts.getInt("followed_by");
            profile.follows = counts.getInt("follows");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return profile;
    }

    public static List<InstagramProfile> fromJson(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        List<InstagramProfile> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            InstagramProfile user = InstagramProfile.fromJson(jsonObject);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstagramProfile that = (InstagramProfile) o;

        if (media != that.media) return false;
        if (followedBy != that.followedBy) return false;
        if (follows != that.follows) return false;
        if (!userName.equals(that.userName)) return false;
        return profilePictureurl.equals(that.profilePictureurl);

    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + profilePictureurl.hashCode();
        result = 31 * result + media;
        result = 31 * result + followedBy;
        result = 31 * result + follows;
        return result;
    }
}
