package com.example.aklati.data.remote.api;

import com.example.aklati.data.remote.responses.CategoryResponse;
import com.example.aklati.data.remote.responses.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ApiService {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("categories.php")
    Single<CategoryResponse> getCategories();
}
