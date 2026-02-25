package com.example.aklati.presentation.search;

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
    }

    interface Presenter {
        void searchMeals(String query);

        void detachView();
    }
}

