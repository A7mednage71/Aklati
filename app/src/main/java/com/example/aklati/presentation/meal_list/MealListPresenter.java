package com.example.aklati.presentation.meal_list;

import android.widget.ImageView;

import com.example.aklati.data.models.MealDetails;
import com.example.aklati.data.repository.FavoriteRepository;
import com.example.aklati.data.repository.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealListPresenter implements MealsListContract.Presenter {
    private final MealRepository repository;
    private final FavoriteRepository favoriteRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MealsListContract.View view;

    public MealListPresenter(MealsListContract.View view, MealRepository repository, FavoriteRepository favoriteRepository) {
        this.view = view;
        this.repository = repository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void getMealsByCategory(String categoryName) {
        if (view == null) return;
        view.showLoading();
        disposables.add(repository.getMealsByCategory(categoryName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mealResponse -> {
            view.hideLoading();
            if (mealResponse != null && mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                view.showMeals(mealResponse.getMeals());
            } else {
                view.showEmptyState();
            }
        }, throwable -> {
            view.hideLoading();
            String errorMsg = throwable.getMessage();
            view.showErrorMessage(errorMsg != null ? errorMsg : "Failed to load meals");
        }));
    }

    @Override
    public void getMealsByArea(String areaName) {
        if (view == null) return;
        view.showLoading();
        disposables.add(repository.getMealsByArea(areaName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mealResponse -> {
            view.hideLoading();
            if (mealResponse != null && mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                view.showMeals(mealResponse.getMeals());
            } else {
                view.showEmptyState();
            }
        }, throwable -> {
            view.hideLoading();
            String errorMsg = throwable.getMessage();
            view.showErrorMessage(errorMsg != null ? errorMsg : "Failed to load meals");
        }));
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
            if (view != null) {
                view.showMessage("Error checking favorite status");
            }
        }));
    }

    @Override
    public void checkFavoriteStatus(String mealId, ImageView favoriteIcon) {
        if (favoriteRepository == null || mealId == null || favoriteIcon == null) return;
        
        disposables.add(favoriteRepository.isFavorite(mealId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(favoriteIcon::setSelected, throwable -> favoriteIcon.setSelected(false)));
    }

    private void addFavoriteToDB(String mealId) {
        disposables.add(repository.getMealById(mealId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mealDetailsResponse -> {
            if (mealDetailsResponse != null && mealDetailsResponse.getMeals() != null && !mealDetailsResponse.getMeals().isEmpty()) {
                MealDetails fullMealDetails = mealDetailsResponse.getMeals().get(0);
                saveFavoriteToDatabase(fullMealDetails, mealId);
            } else {
                if (view != null) {
                    view.showMessage("Failed to get meal details");
                }
            }
        }, throwable -> {
            if (view != null) {
                view.showMessage("Failed to fetch meal details");
            }
        }));
    }

    private void saveFavoriteToDatabase(MealDetails mealDetails, String mealId) {
        disposables.add(favoriteRepository.addFavorite(mealDetails).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            if (view != null) {
                view.showMessage("Added to favorites ❤️");
                view.updateFavoriteIcon(mealId, true);
            }
        }, throwable -> {
            if (view != null) {
                view.showMessage("Failed to add to favorites");
            }
        }));
    }

    private void removeFavoriteFromDB(String mealId) {
        disposables.add(favoriteRepository.removeFavoriteById(mealId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            if (view != null) {
                view.showMessage("Removed from favorites");
                view.updateFavoriteIcon(mealId, false);
            }
        }, throwable -> {
            if (view != null) {
                view.showMessage("Failed to remove from favorites");
            }
        }));
    }

    @Override
    public void detachView() {
        view = null;
        disposables.clear();
    }
}
