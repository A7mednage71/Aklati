package com.example.aklati.presentation.home;


import com.example.aklati.data.models.Category;
import com.example.aklati.data.models.MealDetails;

import java.util.List;

public interface HomeContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showRandomMeal(MealDetails meal);

        void showCategories(List<Category> categories);

        void showUserName(String name);

        void navigateToDetails(MealDetails meal);

        void navigateToSearch();

        void showError(String error);
    }

    interface Presenter {
        void getRandomMeal();

        void getCategories();

        void getUserName();

        void searchMeals(String query);

        void addToFavorites(MealDetails meal);

        void removeFromFavorites(MealDetails meal);

        void detachView();
    }
}