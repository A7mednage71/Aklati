package com.example.aklati.presentation.meal_list;

import android.widget.ImageView;

import com.example.aklati.data.models.Meal;

import java.util.List;

public interface MealsListContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showMeals(List<Meal> meals);

        void showEmptyState();

        void showErrorMessage(String error);

        void showMessage(String message);

        void updateFavoriteIcon(String mealId, boolean isFavorite);
    }

    interface Presenter {
        void getMealsByCategory(String categoryName);

        void getMealsByArea(String areaName);

        void onFavoriteClick(String mealId);

        void checkFavoriteStatus(String mealId, ImageView favoriteIcon);

        void detachView();
    }
}

