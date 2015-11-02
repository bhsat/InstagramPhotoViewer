package com.codepath.instagram.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramSearchTag;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bhavanis on 11/1/15.
 */
public class SearchTagResultsAdapter extends RecyclerView.Adapter<SearchTagResultsAdapter.TagResultsHolder> {

    List<InstagramSearchTag> tags;

    public SearchTagResultsAdapter(List<InstagramSearchTag> tags) {
        this.tags = tags;
    }

    @Override
    public TagResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate custom view
        View tagView = inflater.inflate(R.layout.layout_tag_search, parent, false);

        // return a new holder instance
        return new TagResultsHolder(tagView);
    }

    @Override
    public void onBindViewHolder(TagResultsHolder holder, int position) {
        InstagramSearchTag tag = tags.get(position);
        holder.tvtags.setText("#"+tag.tag);
        if (tag.count > 0) {
            holder.tvTagCount.setText(tag.count + " posts");
        }
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class TagResultsHolder extends RecyclerView.ViewHolder{

        private TextView tvtags;
        private TextView tvTagCount;

        public TagResultsHolder(View itemView) {
            super(itemView);
            tvtags = (TextView) itemView.findViewById(R.id.tvTags);
            tvTagCount = (TextView) itemView.findViewById(R.id.tvTagCount);
        }
    }
}
