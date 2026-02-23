package com.example.aklati.presentation.meal_details;

import com.example.aklati.data.models.Meal;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private MealDetailsContract.View view;

    public MealDetailsPresenter(MealDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void loadMealDetails(Meal meal) {
        if (view == null || meal == null) return;

        // If the meal has no details yet (came from a list), enrich it with dummy details
        if (meal.getInstructions() == null || meal.getInstructions().isEmpty()) {
            Meal full = Meal.dummyDetailMeal();
            // keep original identity fields, copy details
            meal.setInstructions(full.getInstructions());
            meal.setYoutubeUrl(full.getYoutubeUrl());
            meal.setIngredients(
                    full.getIngredients()[0], full.getIngredients()[1],
                    full.getIngredients()[2], full.getIngredients()[3],
                    full.getIngredients()[4], full.getIngredients()[5],
                    full.getIngredients()[6], full.getIngredients()[7],
                    full.getIngredients()[8], full.getIngredients()[9]
            );
            meal.setMeasures(
                    full.getMeasures()[0], full.getMeasures()[1],
                    full.getMeasures()[2], full.getMeasures()[3],
                    full.getMeasures()[4], full.getMeasures()[5],
                    full.getMeasures()[6], full.getMeasures()[7],
                    full.getMeasures()[8], full.getMeasures()[9]
            );
        }

        view.showMealDetails(meal);
        view.updateFavoriteIcon(meal.isFavorite());
    }

    @Override
    public void toggleFavorite(Meal meal) {
        if (view == null || meal == null) return;
        meal.setFavorite(!meal.isFavorite());
        view.updateFavoriteIcon(meal.isFavorite());
    }

    @Override
    public void detachView() {
        view = null;
    }
}

