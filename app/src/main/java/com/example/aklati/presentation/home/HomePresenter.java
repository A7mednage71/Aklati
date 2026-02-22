package com.example.aklati.presentation.home;

import com.example.aklati.data.models.Meal;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void getRandomMeal() {

    }

    @Override
    public void getCategories() {

    }

    @Override
    public void searchMeals(String query) {

    }

    @Override
    public void addToFavorites(Meal meal) {

    }

    @Override
    public void removeFromFavorites(Meal meal) {

    }

    @Override
    public void detachView() {

    }
}