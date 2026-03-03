package com.example.aklati.presentation.search;

import android.util.Log;
import android.widget.ImageView;

import com.example.aklati.data.models.Meal;
import com.example.aklati.data.models.MealDetails;
import com.example.aklati.data.remote.responses.MealResponse;
import com.example.aklati.data.repository.FavoriteRepository;
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
    private static final int DEBOUNCE_TIMEOUT = 500;
    private static final int MIN_SEARCH_LENGTH = 2;

    private final MealRepository repository;
    private final FavoriteRepository favoriteRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();
    private SearchContract.View view;

    public SearchPresenter(SearchContract.View view, MealRepository repository, FavoriteRepository favoriteRepository) {
        this.view = view;
        this.repository = repository;
        this.favoriteRepository = favoriteRepository; // Initialize
        setupSearchObservable();
    }

    private void setupSearchObservable() {
        disposables.add(searchSubject.debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS).distinctUntilChanged().observeOn(AndroidSchedulers.mainThread()).subscribe(this::performSearch, throwable -> {
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
        if (query == null || query.trim().isEmpty()) {
            view.showInitialState();
            disposables.clear();
            setupSearchObservable();
            return;
        }
        String trimmedQuery = query.trim();
        if (trimmedQuery.length() < MIN_SEARCH_LENGTH) {
            view.showInitialState();
            return;
        }
        searchSubject.onNext(trimmedQuery);
    }

    private void performSearch(String query) {
        if (view == null) return;
        view.showLoading();
        disposables.add(repository.searchMeals(query).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> handleSearchSuccess(response, query), throwable -> handleSearchError(throwable, query)));
    }

    private void handleSearchSuccess(MealResponse response, String query) {
        if (view == null) return;
        view.hideLoading();
        if (response == null || response.getMeals() == null || response.getMeals().isEmpty()) {
            view.showEmptyState(query);
            return;
        }
        List<Meal> meals = convertToMealList(response.getMeals());
        if (meals.isEmpty()) {
            view.showEmptyState(query);
        } else {
            view.showMeals(meals);
        }
    }

    private void handleSearchError(Throwable throwable, String query) {
        if (view == null) return;
        Log.e(TAG, "Search failed for query: " + query, throwable);
        view.hideLoading();
        view.showErrorMessage("Failed to search meals. Please try again.");
    }

    @Override
    public void onFavoriteClick(String mealId) {
        if (view == null || favoriteRepository == null || mealId == null) return;
        disposables.add(favoriteRepository.isFavorite(mealId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(isFavorite -> {
            if (isFavorite) {
                removeFavoriteFromDB(mealId);
            } else {
                addFavoriteToDB(mealId);
            }
        }, throwable -> {
            if (view != null) view.showMessage("Error checking favorite status");
        }));
    }

    @Override
    public void checkFavoriteStatus(String mealId, ImageView favoriteIcon) {
        if (favoriteRepository == null || mealId == null || favoriteIcon == null) return;
        disposables.add(favoriteRepository.isFavorite(mealId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(favoriteIcon::setSelected, throwable -> favoriteIcon.setSelected(false)));
    }

    private void addFavoriteToDB(String mealId) {
        disposables.add(repository.getMealById(mealId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mealDetailsResponse -> {
            if (mealDetailsResponse != null && mealDetailsResponse.getMeals() != null && !mealDetailsResponse.getMeals().isEmpty()) {
                MealDetails fullMealDetails = mealDetailsResponse.getMeals().get(0);
                saveFavoriteToDatabase(fullMealDetails, mealId);
            } else {
                if (view != null) view.showMessage("Failed to get meal details");
            }
        }, throwable -> {
            if (view != null) view.showMessage("Failed to fetch meal details");
        }));
    }

    private void saveFavoriteToDatabase(MealDetails mealDetails, String mealId) {
        disposables.add(favoriteRepository.addFavorite(mealDetails).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            if (view != null) {
                view.showMessage("Added to favorites ❤️");
                view.updateFavoriteIcon(mealId, true);
            }
        }, throwable -> {
            if (view != null) view.showMessage("Failed to add to favorites");
        }));
    }

    private void removeFavoriteFromDB(String mealId) {
        disposables.add(favoriteRepository.removeFavoriteById(mealId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            if (view != null) {
                view.showMessage("Removed from favorites");
                view.updateFavoriteIcon(mealId, false);
            }
        }, throwable -> {
            if (view != null) view.showMessage("Failed to remove from favorites");
        }));
    }

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