package com.example.aklati.presentation.favorite;

import com.example.aklati.data.models.MealDetails;

import java.util.List;

public interface FavoriteContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showFavorites(List<MealDetails> mealDetails);

        void showEmptyState();

        void showErrorMessage(String error);
    }

    interface Presenter {
        void getFavorites();

        void removeFavorite(MealDetails mealDetails);

        void detachView();
    }
}

