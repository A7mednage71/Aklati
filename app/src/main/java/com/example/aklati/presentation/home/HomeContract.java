package com.example.aklati.presentation.home;


import com.example.aklati.data.models.Category;
import com.example.aklati.data.models.Meal;

import java.util.List;

public interface HomeContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showRandomMeal(Meal meal);

        void showCategories(List<Category> categories);

        void showUserName(String name);

        void navigateToDetails(Meal meal);

        void navigateToSearch();

        void showError(String error);
    }

    interface Presenter {
        void getRandomMeal();

        void getCategories();

        void getUserName();

        void searchMeals(String query);

        void addToFavorites(Meal meal);

        void removeFromFavorites(Meal meal);

        void detachView();
    }
}