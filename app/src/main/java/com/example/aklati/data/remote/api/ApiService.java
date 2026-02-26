package com.example.aklati.data.remote.api;

import com.example.aklati.data.remote.responses.CategoryResponse;
import com.example.aklati.data.remote.responses.MealDetailsResponse;
import com.example.aklati.data.remote.responses.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("random.php")
    Single<MealDetailsResponse> getRandomMeal();

    @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("lookup.php")
    Single<MealDetailsResponse> getMealById(@Query("i") String mealId);

    @GET("search.php")
    Single<MealDetailsResponse> searchMeals(@Query("s") String query);
}
