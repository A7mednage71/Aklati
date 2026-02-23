package com.example.aklati.presentation.meal_details;

import com.example.aklati.data.models.Meal;

public interface MealDetailsContract {

    interface View {
        void showMealDetails(Meal meal);

        void updateFavoriteIcon(boolean isFavorite);

        void showErrorMessage(String error);
    }

    interface Presenter {
        void loadMealDetails(Meal meal);

        void toggleFavorite(Meal meal);

        void detachView();
    }
}

