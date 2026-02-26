package com.example.aklati.presentation.search;

import android.util.Log;

import com.example.aklati.data.models.Meal;
import com.example.aklati.data.remote.responses.MealResponse;
import com.example.aklati.data.repository.MealRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchPresenter implements SearchContract.Presenter {

    private static final String TAG = "SearchPresenter";
    private static final int DEBOUNCE_TIMEOUT = 500; // milliseconds
    private static final int MIN_SEARCH_LENGTH = 2; // minimum characters to search
    private final MealRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();
    private SearchContract.View view;

    public SearchPresenter(SearchContract.View view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
        setupSearchObservable();
    }

    // Setup debounced search observable
    private void setupSearchObservable() {
        disposables.add(searchSubject.debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .distinctUntilChanged().observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::performSearch, throwable -> {
                    Log.e(TAG, "Search subject error", throwable);
                    if (view != null) {
                        view.hideLoading();
                        view.showErrorMessage("Search failed");
                    }
                }));
    }

    @Override
    public void searchMeals(String query) {
        if (view == null) return;
        // Handle empty query
        if (query == null || query.trim().isEmpty()) {
            view.showInitialState();
            disposables.clear();
            setupSearchObservable(); // Re-setup after clearing
            return;
        }
        // Check minimum length
        String trimmedQuery = query.trim();
        if (trimmedQuery.length() < MIN_SEARCH_LENGTH) {
            view.showInitialState();
            return;
        }
        // Push query to subject for debouncing
        searchSubject.onNext(trimmedQuery);
    }

    // Perform actual search after debounce
    private void performSearch(String query) {
        if (view == null) return;
        view.showLoading();
        disposables.add(repository.searchMeals(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> handleSearchSuccess(response, query),
                        throwable -> handleSearchError(throwable, query)));
    }

    /**
     * Handle successful search response
     */
    private void handleSearchSuccess(MealResponse response, String query) {
        if (view == null) return;

        view.hideLoading();

        if (response == null || response.getMeals() == null || response.getMeals().isEmpty()) {
            view.showEmptyState(query);
            return;
        }

        // Convert MealDetails to Meal for display
        List<Meal> meals = convertToMealList(response.getMeals());

        if (meals.isEmpty()) {
            view.showEmptyState(query);
        } else {
            view.showMeals(meals);
        }
    }

    /**
     * Handle search error
     */
    private void handleSearchError(Throwable throwable, String query) {
        if (view == null) return;

        Log.e(TAG, "Search failed for query: " + query, throwable);
        view.hideLoading();
        view.showErrorMessage("Failed to search meals. Please try again.");
    }

    /**
     * Convert MealDetails list to Meal list
     */
    private List<Meal> convertToMealList(List<Meal> mealDetailsList) {
        List<Meal> meals = new ArrayList<>();
        for (Meal meal : mealDetailsList) {
            if (meal != null) {
                meals.add(new Meal(meal.getId(), meal.getName(), meal.getImage()));
            }
        }
        return meals;
    }

    @Override
    public void detachView() {
        view = null;
        disposables.clear();
    }
}

