package com.example.aklati.presentation.favorite;

import com.example.aklati.data.models.Meal;

import java.util.List;

public interface FavoriteContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showFavorites(List<Meal> meals);

        void showEmptyState();

        void showErrorMessage(String error);
    }

    interface Presenter {
        void getFavorites();

        void removeFavorite(Meal meal);

        void detachView();
    }
}

