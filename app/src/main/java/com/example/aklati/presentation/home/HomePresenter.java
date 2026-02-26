package com.example.aklati.presentation.home;

import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.models.MealDetails;
import com.example.aklati.data.repository.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SharedPrefsHelper prefs;

    public HomePresenter(HomeContract.View view, MealRepository repository, SharedPrefsHelper prefs) {
        this.view = view;
        this.repository = repository;
        this.prefs = prefs;
    }

    @Override
    public void getRandomMeal() {
        view.showLoading();
        disposables.add(repository.getRandomMeal().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {
            view.hideLoading();
            if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                view.showRandomMeal(response.getMeals().get(0));
            } else {
                view.showError("No meal found");
            }
        }, throwable -> {
            view.hideLoading();
            view.showError(throwable.getMessage());
        }));

    }

    @Override
    public void getCategories() {
        view.showLoading();
        disposables.add(repository.getCategories().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {
            view.hideLoading();
            view.showCategories(response.getCategories());
        }, throwable -> {
            view.hideLoading();
            view.showError(throwable.getMessage());
        }));
    }

    @Override
    public void getUserName() {
        try {
            String name = null;
            if (prefs.isLoggedIn()) {
                name = prefs.getData(SharedPrefsHelper.KEY_USER_NAME);
            }
            view.showUserName(name != null && !name.trim().isEmpty() ? name : "Guest");
        } catch (Exception e) {
            view.showUserName("Guest");
        }
    }

    @Override
    public void searchMeals(String query) {

    }

    @Override
    public void addToFavorites(MealDetails meal) {

    }

    @Override
    public void removeFromFavorites(MealDetails meal) {

    }

    @Override
    public void detachView() {

    }
}