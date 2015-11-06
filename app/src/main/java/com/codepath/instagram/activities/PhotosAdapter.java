package com.codepath.instagram.activities;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramImage;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by bhavanis on 11/5/15.
 */
public class PhotosAdapter extends
        RecyclerView.Adapter<PhotosAdapter.PhotosAdapterViewHolder>{
    List<InstagramImage> images;

    public PhotosAdapter(List<InstagramImage> images) {

        this.images = images;
    }

    @Override
    public PhotosAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.layout_item_photos, parent, false);
        return new PhotosAdapterViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(PhotosAdapterViewHolder holder, int position) {
        InstagramImage image = images.get(position);
        holder.ivPhotos.setImageURI(Uri.parse(image.imageUrl));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class PhotosAdapterViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView ivPhotos;

        public PhotosAdapterViewHolder(View itemView) {
            super(itemView);
            ivPhotos = (SimpleDraweeView) itemView.findViewById(R.id.ivPhotos);
        }
    }
}
