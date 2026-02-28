package com.example.aklati.utils;

import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.aklati.R;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

public class ImageHelper {

    private static ShimmerDrawable getShimmerPlaceholder() {
        //  custom Shimmer effect
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setDuration(1200)
                .setBaseColor(Color.parseColor("#FDFDFD"))
                .setHighlightColor(Color.parseColor("#FFFFFF"))
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setBaseAlpha(1.0f)
                .setHighlightAlpha(1.0f)
                .setAutoStart(true)
                .build();

        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        return shimmerDrawable;
    }

    public static void loadImage(ImageView imageView, String url) {
        if (imageView == null || url == null) return;

        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(getShimmerPlaceholder()) // Use Shimmer as placeholder
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .error(R.drawable.aklati_logo)
                .centerCrop()
                .into(imageView);
    }
}