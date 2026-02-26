package com.example.aklati.presentation.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aklati.R;
import com.example.aklati.data.models.MealDetails;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

public class FavoriteMealAdapter extends RecyclerView.Adapter<FavoriteMealAdapter.ViewHolder> {

    private final List<MealDetails> mealDetails;
    private final OnFavoriteActionListener listener;

    public FavoriteMealAdapter(List<MealDetails> mealDetails, OnFavoriteActionListener listener) {
        this.mealDetails = mealDetails;
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
        MealDetails mealDetails = this.mealDetails.get(position);
        holder.bind(mealDetails, listener, position);
    }

    @Override
    public int getItemCount() {
        return mealDetails.size();
    }

    public void removeItem(int position) {
        mealDetails.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mealDetails.size());
    }

    public MealDetails getMealAt(int position) {
        return mealDetails.get(position);
    }

    public void restoreItem(MealDetails mealDetails, int position) {
        this.mealDetails.add(position, mealDetails);
        notifyItemInserted(position);
    }

    public interface OnFavoriteActionListener {
        void onRemoveFavorite(MealDetails mealDetails, int position);

        void onMealClick(MealDetails mealDetails);
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

        void bind(MealDetails mealDetails, OnFavoriteActionListener listener, int position) {
            tvMealName.setText(mealDetails.getName() != null ? mealDetails.getName() : "Unknown");
            tvMealCategory.setText(mealDetails.getCategory() != null ? mealDetails.getCategory() : "");
            tvMealArea.setText(mealDetails.getArea() != null ? mealDetails.getArea() : "");

            // TODO: load image with Glide/Picasso when implementing network
            // Glide.with(itemView.getContext()).load(mealDetails.getImage()).into(ivMealImage);
            ivMealImage.setImageResource(R.drawable.aklati_logo);

            ibRemoveFavorite.setOnClickListener(v -> {
                if (listener != null) listener.onRemoveFavorite(mealDetails, position);
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMealClick(mealDetails);
            });
        }
    }
}


