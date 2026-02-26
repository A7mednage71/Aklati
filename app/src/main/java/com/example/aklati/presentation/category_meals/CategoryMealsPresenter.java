package com.example.aklati.presentation.category_meals;

import com.example.aklati.data.repository.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryMealsPresenter implements CategoryMealsContract.Presenter {
    private final MealRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private CategoryMealsContract.View view;

    public CategoryMealsPresenter(CategoryMealsContract.View view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
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
    public void detachView() {
        view = null;
        disposables.clear();
    }
}
