package com.example.aklati.presentation.meal_details;

import com.example.aklati.data.models.MealDetails;

public interface MealDetailsContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showMealDetails(MealDetails mealDetails);

        void updateFavoriteIcon(boolean isFavorite);

        void showErrorMessage(String error);
    }

    interface Presenter {
        void loadMealDetails(String mealId);

        void toggleFavorite(MealDetails mealDetails);

        void detachView();
    }
}

