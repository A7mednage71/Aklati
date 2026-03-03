package com.example.aklati.presentation.meal_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aklati.R;
import com.example.aklati.data.models.Meal;
import com.example.aklati.utils.ImageHelper;

import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealViewHolder> {

    private final List<Meal> meals;
    private final OnMealClickListener listener;
    private final OnFavoriteClickListener favoriteListener;

    public MealListAdapter(List<Meal> meals, OnMealClickListener listener, OnFavoriteClickListener favoriteListener) {
        this.meals = meals;
        this.listener = listener;
        this.favoriteListener = favoriteListener;
    }

    public MealListAdapter(List<Meal> meals, OnMealClickListener listener) {
        this.meals = meals;
        this.listener = listener;
        this.favoriteListener = null;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_grid, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = this.meals.get(position);
        holder.tvMealName.setText(meal.getName());
        ImageHelper.loadImage(holder.ivMealImage, meal.getImage());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMealClick(meal);
            }
        });

        // Handle favorite click
        holder.ivFavorite.setOnClickListener(v -> {
            if (favoriteListener != null) {
                favoriteListener.onFavoriteClick(meal, position);
            }
        });

        // Check if meal is favorite
        if (favoriteListener != null) {
            favoriteListener.checkFavoriteStatus(meal, holder.ivFavorite);
        }
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void updateFavoriteIcon(String mealId, boolean isFavorite, RecyclerView recyclerView) {
        if (recyclerView == null || meals == null) return;

        for (int i = 0; i < meals.size(); i++) {
            Meal meal = meals.get(i);
            if (meal != null && mealId.equals(meal.getId())) {
                MealViewHolder viewHolder = (MealViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder != null && viewHolder.ivFavorite != null) {
                    viewHolder.ivFavorite.setSelected(isFavorite);
                }
                break;
            }
        }
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Meal meal, int position);

        void checkFavoriteStatus(Meal meal, ImageView favoriteIcon);
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealImage;
        ImageView ivFavorite;
        TextView tvMealName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.ivMealImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvMealName = itemView.findViewById(R.id.tvMealName);
        }
    }
}

