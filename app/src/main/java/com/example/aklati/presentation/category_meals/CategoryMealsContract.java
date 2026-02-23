package com.example.aklati.presentation.category_meals;

import com.example.aklati.data.models.Meal;

import java.util.List;

public interface CategoryMealsContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showMeals(List<Meal> meals);

        void showEmptyState();

        void showErrorMessage(String error);
    }

    interface Presenter {
        void getMealsByCategory(String categoryName);

        void detachView();
    }
}

