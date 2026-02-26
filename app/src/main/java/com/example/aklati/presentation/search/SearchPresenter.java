package com.example.aklati.presentation.search;

import com.example.aklati.data.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    // Full meal database (same dummy data used across the app)
    private static final List<Meal> ALL_MEAL_DETAILS = new ArrayList<>();

    private SearchContract.View view;

    public SearchPresenter(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void searchMeals(String query) {
        if (view == null) return;

        if (query == null || query.trim().isEmpty()) {
            view.showInitialState();
            return;
        }

        view.showLoading();

        String lowerQuery = query.trim().toLowerCase();
        List<Meal> results = new ArrayList<>();

        view.hideLoading();

        if (results.isEmpty()) {
            view.showEmptyState(query);
        } else {
            view.showMeals(results);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}

