package com.example.aklati.presentation.meal_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aklati.R;

import java.util.List;

public class IngredientAdapter {
    public static void populate(Context context, LinearLayout container,
                                List<String> names, List<String> measures) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(context);

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String measure = (i < measures.size()) ? measures.get(i) : "";

            if (name == null || name.trim().isEmpty()) continue;

            View item = inflater.inflate(R.layout.item_ingredient, container, false);

            ImageView ivImage = item.findViewById(R.id.ivIngredientImage);
            TextView tvName = item.findViewById(R.id.tvIngredientName);
            TextView tvMeasure = item.findViewById(R.id.tvIngredientMeasure);

            tvName.setText(name);
            tvMeasure.setText(measure != null ? measure.trim() : "");

            // TheMealDB hosts ingredient thumbnails at this URL
            String imageUrl = "https://www.themealdb.com/images/ingredients/" +
                    name.replace(" ", "%20") + "-Small.png";

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.aklati_logo)
                    .error(R.drawable.aklati_logo)
                    .into(ivImage);

            container.addView(item);
        }
    }
}

