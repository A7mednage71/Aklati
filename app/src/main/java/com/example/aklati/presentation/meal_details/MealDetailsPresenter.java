package com.example.aklati.presentation.meal_details;

import android.util.Log;

import com.example.aklati.data.models.MealDetails;
import com.example.aklati.data.remote.responses.MealDetailsResponse;
import com.example.aklati.data.repository.MealRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private static final String TAG = "MealDetailsPresenter";
    private final MealRepository repository;
    private final CompositeDisposable disposables;
    private MealDetailsContract.View view;
    private MealDetails currentMeal;

    public MealDetailsPresenter(MealDetailsContract.View view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void loadMealDetails(String mealId) {
        if (view == null || mealId == null || mealId.isEmpty()) return;

        view.showLoading();

        Disposable disposable = repository.getMealById(mealId)
                .subscribeOn(Schedulers.io()).delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleSuccess,
                        this::handleError
                );

        disposables.add(disposable);
    }

    private void handleSuccess(MealDetailsResponse response) {
        view.hideLoading();

        if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
            currentMeal = response.getMeals().get(0);
            view.showMealDetails(currentMeal);
            // TODO: Check if meal is favorite from Room database
            view.updateFavoriteIcon(false);
        } else {
            view.showErrorMessage("Meal details not found");
        }
    }

    private void handleError(Throwable throwable) {
        view.hideLoading();

        Log.e(TAG, "Error loading meal details", throwable);
        view.showErrorMessage("Failed to load meal details. Please check your internet connection.");
    }

    @Override
    public void toggleFavorite(MealDetails mealDetails) {
        if (view == null || mealDetails == null) {
        }
        // TODO: Save/remove from Room database

    }

    @Override
    public void detachView() {
        view = null;
        disposables.clear();
    }
}

