package com.example.aklati.presentation.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aklati.R;
import com.example.aklati.data.models.Meal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

public class FavoriteMealAdapter extends RecyclerView.Adapter<FavoriteMealAdapter.ViewHolder> {

    private final List<Meal> meals;
    private final OnFavoriteActionListener listener;

    public FavoriteMealAdapter(List<Meal> meals, OnFavoriteActionListener listener) {
        this.meals = meals;
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
        Meal meal = meals.get(position);
        holder.bind(meal, listener, position);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void removeItem(int position) {
        meals.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, meals.size());
    }

    public Meal getMealAt(int position) {
        return meals.get(position);
    }

    public void restoreItem(Meal meal, int position) {
        meals.add(position, meal);
        notifyItemInserted(position);
    }

    public interface OnFavoriteActionListener {
        void onRemoveFavorite(Meal meal, int position);

        void onMealClick(Meal meal);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealImage;
        TextView tvMealName, tvMealArea;
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

        void bind(Meal meal, OnFavoriteActionListener listener, int position) {
            tvMealName.setText(meal.getName() != null ? meal.getName() : "Unknown");
            tvMealCategory.setText(meal.getCategory() != null ? meal.getCategory() : "");
            tvMealArea.setText(meal.getArea() != null ? meal.getArea() : "");

            // TODO: load image with Glide/Picasso when implementing network
            // Glide.with(itemView.getContext()).load(meal.getImage()).into(ivMealImage);
            ivMealImage.setImageResource(R.drawable.aklati_logo);

            ibRemoveFavorite.setOnClickListener(v -> {
                if (listener != null) listener.onRemoveFavorite(meal, position);
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMealClick(meal);
            });
        }
    }
}


