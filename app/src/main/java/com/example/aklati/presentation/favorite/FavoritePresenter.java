package com.example.aklati.presentation.favorite;

import android.util.Log;

import com.example.aklati.data.repository.FavoriteRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenter implements FavoriteContract.Presenter {

    private static final String TAG = "FavoritePresenter";
    private final FavoriteRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private FavoriteContract.View view;

    public FavoritePresenter(FavoriteContract.View view, FavoriteRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getFavorites() {
        if (view != null) {
            view.showLoading();
        }

        disposables.add(repository.getAllFavorites().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(favoriteMeals -> {
            if (view != null) {
                view.hideLoading();
                if (favoriteMeals == null || favoriteMeals.isEmpty()) {
                    view.showEmptyState();
                } else {
                    view.showFavorites(favoriteMeals);
                }
            }
        }, throwable -> {
            Log.e(TAG, "Error loading favorites", throwable);
            if (view != null) {
                view.hideLoading();
                view.showErrorMessage("Failed to load favorites");
            }
        }));
    }

    @Override
    public void removeFavorite(String mealId) {
        disposables.add(repository.removeFavoriteById(mealId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            Log.d(TAG, "Favorite removed successfully");
            // Flowable will auto-update the list
        }, throwable -> {
            Log.e(TAG, "Error removing favorite", throwable);
            if (view != null) {
                view.showErrorMessage("Failed to remove favorite");
            }
        }));
    }

    @Override
    public void detachView() {
        view = null;
        disposables.clear();
    }
}
