package com.example.aklati.presentation.favorite;

import com.example.aklati.data.local.entity.FavoriteMeal;

import java.util.List;

public interface FavoriteContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showFavorites(List<FavoriteMeal> favoriteMeals);

        void showEmptyState();

        void showErrorMessage(String error);
    }

    interface Presenter {
        void getFavorites();

        void removeFavorite(String mealId);

        void detachView();
    }
}

