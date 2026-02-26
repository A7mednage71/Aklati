package com.example.aklati.presentation.meal_details;

import com.example.aklati.data.models.MealDetails;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private MealDetailsContract.View view;

    public MealDetailsPresenter(MealDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void loadMealDetails(MealDetails mealDetails) {
        if (view == null || mealDetails == null) return;

        // If the mealDetails has no details yet (came from a list), enrich it with dummy details
        if (mealDetails.getInstructions() == null || mealDetails.getInstructions().isEmpty()) {
            MealDetails full = MealDetails.dummyDetailMeal();
            // keep original identity fields, copy details
            mealDetails.setInstructions(full.getInstructions());
            mealDetails.setYoutubeUrl(full.getYoutubeUrl());
            mealDetails.setIngredients(
                    full.getIngredients()[0], full.getIngredients()[1],
                    full.getIngredients()[2], full.getIngredients()[3],
                    full.getIngredients()[4], full.getIngredients()[5],
                    full.getIngredients()[6], full.getIngredients()[7],
                    full.getIngredients()[8], full.getIngredients()[9]
            );
            mealDetails.setMeasures(
                    full.getMeasures()[0], full.getMeasures()[1],
                    full.getMeasures()[2], full.getMeasures()[3],
                    full.getMeasures()[4], full.getMeasures()[5],
                    full.getMeasures()[6], full.getMeasures()[7],
                    full.getMeasures()[8], full.getMeasures()[9]
            );
        }

        view.showMealDetails(mealDetails);
        view.updateFavoriteIcon(mealDetails.isFavorite());
    }

    @Override
    public void toggleFavorite(MealDetails mealDetails) {
        if (view == null || mealDetails == null) return;
        mealDetails.setFavorite(!mealDetails.isFavorite());
        view.updateFavoriteIcon(mealDetails.isFavorite());
    }

    @Override
    public void detachView() {
        view = null;
    }
}

