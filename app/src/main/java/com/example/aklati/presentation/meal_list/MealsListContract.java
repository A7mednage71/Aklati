package com.example.aklati.presentation.meal_list;

import com.example.aklati.data.models.Meal;

import java.util.List;

public interface MealsListContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showMeals(List<Meal> meals);

        void showEmptyState();

        void showErrorMessage(String error);
    }

    interface Presenter {
        void getMealsByCategory(String categoryName);

        void getMealsByArea(String areaName);

        void detachView();
    }
}

