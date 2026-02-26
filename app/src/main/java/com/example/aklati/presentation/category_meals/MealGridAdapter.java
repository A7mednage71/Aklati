package com.example.aklati.presentation.category_meals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aklati.R;
import com.example.aklati.data.models.Meal;

import java.util.List;

public class MealGridAdapter extends RecyclerView.Adapter<MealGridAdapter.MealViewHolder> {

    private final List<Meal> meals;
    private final OnMealClickListener listener;

    public MealGridAdapter(List<Meal> meals, OnMealClickListener listener) {
        this.meals = meals;
        this.listener = listener;
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
        
        // Load meal image with Glide
        Glide.with(holder.itemView.getContext())
                .load(meal.getImage())
                .placeholder(R.drawable.aklati_logo)
                .error(R.drawable.aklati_logo)
                .centerCrop()
                .into(holder.ivMealImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMealClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealImage;
        TextView tvMealName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.ivMealImage);
            tvMealName = itemView.findViewById(R.id.tvMealName);
        }
    }
}

