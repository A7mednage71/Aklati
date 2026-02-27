package com.example.aklati.data.repository;

import com.example.aklati.data.remote.api.ApiService;
import com.example.aklati.data.remote.responses.AreaResponse;
import com.example.aklati.data.remote.responses.CategoryResponse;
import com.example.aklati.data.remote.responses.MealDetailsResponse;
import com.example.aklati.data.remote.responses.MealResponse;

import io.reactivex.rxjava3.core.Single;

public class MealRepository {
    final ApiService apiService;

    public MealRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<MealDetailsResponse> getRandomMeal() {
        return apiService.getRandomMeal();
    }

    public Single<CategoryResponse> getCategories() {
        return apiService.getCategories();
    }

    public Single<AreaResponse> getAreas() {
        return apiService.getAreas();
    }

    public Single<MealResponse> getMealsByCategory(String category) {
        return apiService.getMealsByCategory(category);
    }

    public Single<MealDetailsResponse> getMealById(String mealId) {
        return apiService.getMealById(mealId);
    }

    public Single<MealResponse> searchMeals(String query) {
        return apiService.searchMeals(query);
    }

}
