package com.example.aklati.presentation.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aklati.R;
import com.example.aklati.data.local.entity.FavoriteMeal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

public class FavoriteMealAdapter extends RecyclerView.Adapter<FavoriteMealAdapter.ViewHolder> {

    private final List<FavoriteMeal> favoriteMeals;
    private final OnFavoriteActionListener listener;

    public FavoriteMealAdapter(List<FavoriteMeal> favoriteMeals, OnFavoriteActionListener listener) {
        this.favoriteMeals = favoriteMeals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteMeal meal = this.favoriteMeals.get(position);
        holder.bind(meal, listener, position);
    }

    @Override
    public int getItemCount() {
        return favoriteMeals.size();
    }

    public void removeItem(int position) {
        favoriteMeals.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favoriteMeals.size());
    }

    public FavoriteMeal getMealAt(int position) {
        return favoriteMeals.get(position);
    }

    public void restoreItem(FavoriteMeal meal, int position) {
        this.favoriteMeals.add(position, meal);
        notifyItemInserted(position);
    }

    public interface OnFavoriteActionListener {
        void onRemoveFavorite(FavoriteMeal meal, int position);

        void onMealClick(FavoriteMeal meal);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealImage;
        TextView tvMealName;
        TextView tvMealArea;
        Chip tvMealCategory;
        MaterialButton ibRemoveFavorite;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.ivFavMealImage);
            tvMealName = itemView.findViewById(R.id.tvFavMealName);
            tvMealCategory = itemView.findViewById(R.id.tvFavMealCategory);
            tvMealArea = itemView.findViewById(R.id.tvFavMealArea);
            ibRemoveFavorite = itemView.findViewById(R.id.btnRemoveFavorite);
        }

        void bind(FavoriteMeal meal, OnFavoriteActionListener listener, int position) {
            // Name
            tvMealName.setText(meal.getMealName() != null ? meal.getMealName() : "Unknown");

            // Category
            if (meal.getMealCategory() != null && !meal.getMealCategory().isEmpty()) {
                tvMealCategory.setText(meal.getMealCategory());
                tvMealCategory.setVisibility(View.VISIBLE);
            } else {
                tvMealCategory.setVisibility(View.GONE);
            }

            // Area
            if (meal.getMealArea() != null && !meal.getMealArea().isEmpty()) {
                tvMealArea.setText(meal.getMealArea());
                tvMealArea.setVisibility(View.VISIBLE);
            } else {
                tvMealArea.setVisibility(View.GONE);
            }

            // Load image with Glide
            if (meal.getMealImage() != null && !meal.getMealImage().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(meal.getMealImage())
                        .placeholder(R.drawable.aklati_logo)
                        .error(R.drawable.aklati_logo)
                        .centerCrop()
                        .into(ivMealImage);
            } else {
                ivMealImage.setImageResource(R.drawable.aklati_logo);
            }

            // Remove button
            ibRemoveFavorite.setOnClickListener(v -> {
                if (listener != null) listener.onRemoveFavorite(meal, position);
            });

            // Item click
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMealClick(meal);
            });
        }
    }
}
