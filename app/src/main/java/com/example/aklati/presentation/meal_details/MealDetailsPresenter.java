package com.example.aklati.presentation.meal_details;

import android.util.Log;

import com.example.aklati.data.models.MealDetails;
import com.example.aklati.data.remote.responses.MealDetailsResponse;
import com.example.aklati.data.repository.FavoriteRepository;
import com.example.aklati.data.repository.MealRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private static final String TAG = "MealDetailsPresenter";
    private final MealRepository mealRepository;
    private final FavoriteRepository favoriteRepository;
    private final CompositeDisposable disposables;
    private MealDetailsContract.View view;
    private MealDetails currentMeal;
    private boolean isFavorite = false;

    public MealDetailsPresenter(MealDetailsContract.View view, MealRepository mealRepository, FavoriteRepository favoriteRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
        this.favoriteRepository = favoriteRepository;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void loadMealDetails(String mealId) {
        if (view == null || mealId == null || mealId.isEmpty()) return;

        view.showLoading();

        Disposable disposable = mealRepository.getMealById(mealId).subscribeOn(Schedulers.io()).delay(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleSuccess, this::handleError);

        disposables.add(disposable);
    }

    private void handleSuccess(MealDetailsResponse response) {
        view.hideLoading();

        if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
            currentMeal = response.getMeals().get(0);
            view.showMealDetails(currentMeal);

            // Check if meal is already in favorites
            checkIfFavorite(currentMeal.getId());
        } else {
            view.showErrorMessage("Meal details not found");
        }
    }

    private void handleError(Throwable throwable) {
        view.hideLoading();

        Log.e(TAG, "Error loading meal details", throwable);
        view.showErrorMessage("Failed to load meal details. Please check your internet connection.");
    }

    private void checkIfFavorite(String mealId) {
        disposables.add(favoriteRepository.isFavorite(mealId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(favorite -> {
            isFavorite = favorite;
            if (view != null) {
                view.updateFavoriteIcon(isFavorite);
            }
            Log.d(TAG, "Favorite status checked: " + isFavorite);
        }, throwable -> {
            Log.e(TAG, "Error checking favorite status", throwable);
            isFavorite = false;
            if (view != null) {
                view.updateFavoriteIcon(false);
            }
        }));
    }

    @Override
    public void toggleFavorite(MealDetails mealDetails) {
        if (view == null || mealDetails == null) return;

        if (isFavorite) {
            // Remove from favorites
            removeFavorite(mealDetails);
        } else {
            // Add to favorites
            addFavorite(mealDetails);
        }
    }

    private void addFavorite(MealDetails mealDetails) {
        disposables.add(favoriteRepository.addFavorite(mealDetails).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            isFavorite = true;
            if (view != null) {
                view.updateFavoriteIcon(true);
                view.showMessage("Added to favorites ❤️");
            }
            Log.d(TAG, "Meal added to favorites");
        }, throwable -> {
            Log.e(TAG, "Error adding to favorites", throwable);
            if (view != null) {
                view.showErrorMessage("Failed to add to favorites: " + throwable.getMessage());
            }
        }));
    }

    private void removeFavorite(MealDetails mealDetails) {
        disposables.add(favoriteRepository.removeFavoriteById(mealDetails.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            isFavorite = false;
            if (view != null) {
                view.updateFavoriteIcon(false);
                view.showMessage("Removed from favorites");
            }
            Log.d(TAG, "Meal removed from favorites");
        }, throwable -> {
            Log.e(TAG, "Error removing from favorites", throwable);
            if (view != null) {
                view.showErrorMessage("Failed to remove from favorites: " + throwable.getMessage());
            }
        }));
    }

    @Override
    public void detachView() {
        view = null;
        disposables.clear();
    }
}

