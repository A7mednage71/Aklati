package com.example.aklati.data.local.mapper;

import com.example.aklati.data.local.entity.FavoriteMeal;
import com.example.aklati.data.models.Meal;
import com.example.aklati.data.models.MealDetails;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMealMapper {

    public static FavoriteMeal toFavoriteMeal(String userId, MealDetails mealDetails) {
        if (mealDetails == null || userId == null) return null;

        return new FavoriteMeal(
                userId,
                mealDetails.getId(),
                mealDetails.getName(),
                mealDetails.getImage(),
                mealDetails.getCategory(),
                mealDetails.getArea()
        );
    }

    public static Meal toMeal(FavoriteMeal favoriteMeal) {
        if (favoriteMeal == null) return null;

        return new Meal(
                favoriteMeal.getMealId(),
                favoriteMeal.getMealName(),
                favoriteMeal.getMealImage()
        );
    }

    public static List<Meal> toMealList(List<FavoriteMeal> favoriteMeals) {
        List<Meal> meals = new ArrayList<>();
        if (favoriteMeals != null) {
            for (FavoriteMeal favoriteMeal : favoriteMeals) {
                Meal meal = toMeal(favoriteMeal);
                if (meal != null) {
                    meals.add(meal);
                }
            }
        }
        return meals;
    }
}



