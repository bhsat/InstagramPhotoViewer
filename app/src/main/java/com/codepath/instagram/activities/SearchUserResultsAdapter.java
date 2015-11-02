package com.codepath.instagram.activities;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by bhavanis on 11/1/15.
 */
public class SearchUserResultsAdapter extends RecyclerView.Adapter<SearchUserResultsAdapter.SearchResultsHolder> {

    static List<InstagramUser> users;

    public SearchUserResultsAdapter(List<InstagramUser> users) {
        this.users = users;
    }

    @Override
    public SearchResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate custom view
        View userView = inflater.inflate(R.layout.layout_user_search, parent, false);

        // return a new holder instance
        return new SearchResultsHolder(userView);
    }

    @Override
    public void onBindViewHolder(SearchResultsHolder holder, int position) {
        InstagramUser user = users.get(position);
        holder.ivProfilePic.setImageURI(Uri.parse(user.profilePictureUrl));
        holder.tvUsername.setText(user.userName);
        holder.tvFullName.setText(user.fullName);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class SearchResultsHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView ivProfilePic;
        private TextView tvUsername;
        private TextView tvFullName;

        public SearchResultsHolder(View itemView) {
            super(itemView);
            ivProfilePic = (SimpleDraweeView) itemView.findViewById(R.id.ivProfilePic);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvFullName = (TextView) itemView.findViewById(R.id.tvFullName);
        }
    }
}
