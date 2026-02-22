package com.example.aklati.presentation.favorite;

import com.example.aklati.data.models.Meal;

import java.util.List;

public class FavoritePresenter implements FavoriteContract.Presenter {

    private FavoriteContract.View view;

    public FavoritePresenter(FavoriteContract.View view) {
        this.view = view;
    }

    @Override
    public void getFavorites() {
        // TODO: implement real DB/repo logic
        if (view != null) {
            List<Meal> dummyList = Meal.dummyMeals();
            if (dummyList.isEmpty()) {
                view.showEmptyState();
            } else {
                view.showFavorites(dummyList);
            }
        }
    }

    @Override
    public void removeFavorite(Meal meal) {
        // TODO: implement remove from DB/repo logic
    }

    @Override
    public void detachView() {
        view = null;
    }
}

