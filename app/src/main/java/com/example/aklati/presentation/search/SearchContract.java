package com.example.aklati.presentation.search;

import android.widget.ImageView;

import com.example.aklati.data.models.Meal;

import java.util.List;

public interface SearchContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showMeals(List<Meal> meals);

        void showEmptyState(String query);

        void showInitialState();

        void showErrorMessage(String error);

        void showMessage(String message);

        void updateFavoriteIcon(String mealId, boolean isFavorite);
    }

    interface Presenter {
        void searchMeals(String query);

        void detachView();

        void onFavoriteClick(String mealId);

        void checkFavoriteStatus(String mealId, ImageView favoriteIcon);
    }
}