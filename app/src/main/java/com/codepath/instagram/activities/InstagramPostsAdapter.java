package com.codepath.instagram.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.List;

/**
 * Created by bhavanis on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostItemViewHolder> {
    static List<InstagramPost> posts;
    ForegroundColorSpan blueColorSpan;
    ForegroundColorSpan grayColorSpan;
    LayoutInflater inflater;
    private static final String TAG = "PostAdapter";

    public InstagramPostsAdapter(List<InstagramPost> posts) {
        this.posts = posts;
    }

    @Override
    public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        inflater = LayoutInflater.from(context);

        // inflate custom view
        View postView = inflater.inflate(R.layout.layout_item_post, parent, false);

        blueColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.blue_text));
        grayColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.gray_text));

        // return a new holder instance
        return new PostItemViewHolder(context, postView);
    }

    @Override
    public void onBindViewHolder(PostItemViewHolder holder, int position) {
        InstagramPost post = posts.get(position);
        holder.llComents.removeAllViews();
        //Log.i(TAG, String.valueOf(post.commentsCount));
        if (post.commentsCount == 0) {
            holder.llComents.setVisibility(View.GONE);
            holder.btnComments.setVisibility(View.GONE);
        } else if (post.commentsCount <= 2) {
            holder.llComents.setVisibility(View.VISIBLE);
            holder.btnComments.setVisibility(View.GONE);
            for (InstagramComment comment : post.comments) {
                View commentView = inflater.inflate(R.layout.layout_item_text_comment, holder.llComents, false);
                TextView tvComment = (TextView) commentView.findViewById(R.id.tvSingleComment);
                tvComment.setText(setSpan(comment.user.userName, comment.text));
                holder.llComents.addView(commentView);
            }
        } else if (post.commentsCount > 2) {
            holder.llComents.setVisibility(View.VISIBLE);
            holder.btnComments.setVisibility(View.VISIBLE);
            holder.btnComments.setText("View all "+post.commentsCount+" comments");
            int size = post.comments.size() - 1;
            for (int i=size-1; i < post.comments.size();i++) {
                View commentView = inflater.inflate(R.layout.layout_item_text_comment, holder.llComents, false);
                TextView tvComment = (TextView) commentView.findViewById(R.id.tvSingleComment);
                tvComment.setText(setSpan(post.comments.get(i).user.userName, post.comments.get(i).text));
                holder.llComents.addView(commentView);
            }
        }
        Uri imageUri = Uri.parse(post.user.profilePictureUrl);
        holder.ivProfilePic.setImageURI(imageUri);
        holder.tvUser.setText(post.user.userName);
        holder.tvTime.setText(getTimeElapsed(post.createdTime));
        holder.tvLikes.setText(getLikesCount(post.likesCount));
        holder.ivImage.setImageURI(Uri.parse(post.image.imageUrl));
        holder.ivImage.setAspectRatio(getAspectRatio(post.image.imageHeight, post.image.imageWidth));
        if (post.caption == null) {
            holder.tvUser2.setVisibility(View.GONE);
        } else {
            holder.tvUser2.setText(setSpan(post.user.userName, post.caption), TextView.BufferType.EDITABLE);
        }
    }

    private String getTimeElapsed(long time) {
        return DateUtils.getRelativeTimeSpanString(time * 1000,
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
    }

    private String getLikesCount(int likesCount) {
        if (likesCount > 1)
            return Integer.toString(likesCount) + " likes";
        else
            return Integer.toString(likesCount) + " like";
    }

    private float getAspectRatio (int height, int width) {
        return (width/height);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostItemViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView ivProfilePic;
        public TextView tvUser;
        public TextView tvTime;
        public SimpleDraweeView ivImage;
        public TextView tvLikes;
        public TextView tvUser2;
        public TextView tvComments;
        public LinearLayout llComents;
        public Button btnComments;
        private Context context;
        public ImageButton iBtnDots;
        Bitmap mBitmap;

        public PostItemViewHolder(Context ctx , View layoutView) {
            super(layoutView);
            this.context = ctx;
            ivProfilePic = (SimpleDraweeView) layoutView.findViewById(R.id.roundView);
            tvUser = (TextView) layoutView.findViewById(R.id.tvUser);
            tvTime = (TextView) layoutView.findViewById(R.id.tvTime);
            ivImage = (SimpleDraweeView) layoutView.findViewById(R.id.imageView);
            tvLikes = (TextView) layoutView.findViewById(R.id.tvLikes);
            tvUser2 = (TextView) layoutView.findViewById(R.id.tvUser2);
            tvComments = (TextView) layoutView.findViewById(R.id.tvComments);
            llComents = (LinearLayout) layoutView.findViewById(R.id.llComments);
            btnComments = (Button) layoutView.findViewById(R.id.btnComments);
            iBtnDots = (ImageButton) layoutView.findViewById(R.id.iBtnDots);
            btnComments.setTransformationMethod(null);
            btnComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getPosition();
                    InstagramPost post =  posts.get(position);
                    Intent i = new Intent(context, CommentsActivity.class);
                    i.putExtra("mediaId", post.mediaId);
                    context.startActivity(i);
                }
            });
            iBtnDots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getPosition();
                    InstagramPost post = posts.get(pos);
                    getBitmap(post.image.imageUrl);
                    shareBitmap(mBitmap);
                }
            });
        }
        public void getBitmap(String url) {
            ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(url));
            ImagePipeline imagePipeline = Fresco.getImagePipeline();

            DataSource<CloseableReference<CloseableImage>> dataSource =
                    imagePipeline.fetchDecodedImage(imageRequest, this);

            try {
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    public void onNewResultImpl(@Nullable Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }
                        mBitmap = bitmap;

                    }

                    @Override
                    public void onFailureImpl(DataSource dataSource) {
                        // No cleanup required here
                    }
                }, CallerThreadExecutor.getInstance());
            } finally {
                if (dataSource != null) {
                    dataSource.close();
                }
            }
        }

        public void shareBitmap(Bitmap bitmap) {
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    bitmap, "Image Description", null);
            Uri bmpUri = Uri.parse(path);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");

            context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
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

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of instagram posts
    public void addAll(List<InstagramPost> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
