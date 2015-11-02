package com.codepath.instagram.activities;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bhavanis on 10/29/15.
 */
public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.CommentsViewHolder> {

    List<InstagramComment> comments;
    ForegroundColorSpan blueColorSpan;
    ForegroundColorSpan grayColorSpan;

    public InstagramCommentsAdapter(List<InstagramComment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate custom view
        View commentsView = inflater.inflate(R.layout.layout_item_comment, parent, false);

        blueColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.blue_text));
        grayColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.gray_text));

        // return a new holder instance
        return new CommentsViewHolder(commentsView);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        InstagramComment comment = comments.get(position);
        holder.tvComment.setText(setSpan(comment.user.userName, comment.text));
        holder.icon.setImageURI(Uri.parse(comment.user.profilePictureUrl));
        holder.tvTime.setText(getTimeElapsed(comment.createdTime));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    private String getTimeElapsed(long time) {
        return DateUtils.getRelativeTimeSpanString(time * 1000,
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView icon;
        public TextView tvComment;
        public TextView tvTime;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            icon = (SimpleDraweeView) itemView.findViewById(R.id.ivProfile);
            tvComment = (TextView) itemView.findViewById(R.id.tvEachComment);
            tvTime = (TextView) itemView.findViewById(R.id.tvCommentTime);
        }
    }

    private SpannableStringBuilder setSpan(String firstWord, String secondWord) {
        // Use a SpannableStringBuilder so that both the text and the spans are mutable
        SpannableStringBuilder ssb = new SpannableStringBuilder(firstWord);
        TypefaceSpan typeSpan = new TypefaceSpan("sans-serif-medium");

        // Apply the color span
        ssb.setSpan(
                blueColorSpan,
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(typeSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Add a blank space
        ssb.append(" ");

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
