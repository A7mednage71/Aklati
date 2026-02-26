package com.example.aklati.data.local.mapper;

import com.example.aklati.data.local.entity.FavoriteMeal;
import com.example.aklati.data.models.Meal;
import com.example.aklati.data.models.MealDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class to convert between MealDetails and FavoriteMeal entities
 * Only stores basic info: userId, mealId, name, image, category, area
 */
public class FavoriteMealMapper {

    /**
     * Convert MealDetails to FavoriteMeal for Room storage
     *
     * @param userId      Current user ID
     * @param mealDetails Meal details from API
     */
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

    /**
     * Convert FavoriteMeal from Room to Meal (for display in favorites list)
     */
    public static Meal toMeal(FavoriteMeal favoriteMeal) {
        if (favoriteMeal == null) return null;

        return new Meal(
                favoriteMeal.getMealId(),
                favoriteMeal.getMealName(),
                favoriteMeal.getMealImage()
        );
    }

    /**
     * Convert list of FavoriteMeal to list of Meal
     */
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



