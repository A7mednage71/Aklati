package com.example.aklati.data.remote.responses;

import com.example.aklati.data.models.MealDetails;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealDetailsResponse {
    @SerializedName("meals")
    private List<MealDetails> meals;

    public List<MealDetails> getMeals() {
        return meals;
    }
}
