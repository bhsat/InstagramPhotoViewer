package com.codepath.instagram.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bhavanis on 11/2/15.
 */
public class InstagramPostsWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<InstagramPost> posts;

    public InstagramPostsWrapper(List<InstagramPost> posts) {
        this.posts = posts;
    }

    public List<InstagramPost> getPosts() {
        return posts;
    }

}
