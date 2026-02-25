package com.example.aklati.presentation.search;

import com.example.aklati.data.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    // Full meal database (same dummy data used across the app)
    private static final List<Meal> ALL_MEALS = new ArrayList<>();

    static {
        // Beef
        ALL_MEALS.add(new Meal("1", "Beef Wellington", null, "British", "Beef"));
        ALL_MEALS.add(new Meal("2", "Beef Stroganoff", null, "Russian", "Beef"));
        ALL_MEALS.add(new Meal("3", "Beef Rendang", null, "Malaysian", "Beef"));
        ALL_MEALS.add(new Meal("4", "Beef Tacos", null, "Mexican", "Beef"));

        // Chicken
        ALL_MEALS.add(new Meal("5", "Chicken Tikka Masala", null, "Indian", "Chicken"));
        ALL_MEALS.add(new Meal("6", "Grilled Chicken", null, "American", "Chicken"));
        ALL_MEALS.add(new Meal("7", "Chicken Alfredo", null, "Italian", "Chicken"));
        ALL_MEALS.add(new Meal("8", "Chicken Shawarma", null, "Lebanese", "Chicken"));

        // Dessert
        ALL_MEALS.add(new Meal("9", "Chocolate Lava Cake", null, "French", "Dessert"));
        ALL_MEALS.add(new Meal("10", "Crème Brûlée", null, "French", "Dessert"));
        ALL_MEALS.add(new Meal("11", "Tiramisu", null, "Italian", "Dessert"));
        ALL_MEALS.add(new Meal("12", "Baklava", null, "Turkish", "Dessert"));

        // Lamb
        ALL_MEALS.add(new Meal("13", "Lamb Chops", null, "Greek", "Lamb"));
        ALL_MEALS.add(new Meal("14", "Lamb Tagine", null, "Moroccan", "Lamb"));
        ALL_MEALS.add(new Meal("15", "Lamb Kofta", null, "Lebanese", "Lamb"));

        // Pasta
        ALL_MEALS.add(new Meal("16", "Spaghetti Bolognese", null, "Italian", "Pasta"));
        ALL_MEALS.add(new Meal("17", "Penne Arrabbiata", null, "Italian", "Pasta"));
        ALL_MEALS.add(new Meal("18", "Fettuccine Alfredo", null, "Italian", "Pasta"));
        ALL_MEALS.add(new Meal("19", "Lasagna", null, "Italian", "Pasta"));

        // Seafood
        ALL_MEALS.add(new Meal("20", "Grilled Salmon", null, "Japanese", "Seafood"));
        ALL_MEALS.add(new Meal("21", "Shrimp Scampi", null, "Italian", "Seafood"));
        ALL_MEALS.add(new Meal("22", "Fish and Chips", null, "British", "Seafood"));
        ALL_MEALS.add(new Meal("23", "Lobster Bisque", null, "French", "Seafood"));

        // Vegan
        ALL_MEALS.add(new Meal("24", "Vegan Buddha Bowl", null, "American", "Vegan"));
        ALL_MEALS.add(new Meal("25", "Falafel Wrap", null, "Lebanese", "Vegan"));
        ALL_MEALS.add(new Meal("26", "Vegan Curry", null, "Indian", "Vegan"));

        // Vegetarian
        ALL_MEALS.add(new Meal("27", "Caesar Salad", null, "American", "Vegetarian"));
        ALL_MEALS.add(new Meal("28", "Margherita Pizza", null, "Italian", "Vegetarian"));
        ALL_MEALS.add(new Meal("29", "Caprese Salad", null, "Italian", "Vegetarian"));
        ALL_MEALS.add(new Meal("30", "Stuffed Peppers", null, "Greek", "Vegetarian"));
    }

    private SearchContract.View view;

    public SearchPresenter(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void searchMeals(String query) {
        if (view == null) return;

        if (query == null || query.trim().isEmpty()) {
            view.showInitialState();
            return;
        }

        view.showLoading();

        String lowerQuery = query.trim().toLowerCase();
        List<Meal> results = new ArrayList<>();

        for (Meal meal : ALL_MEALS) {
            if (meal.getName() != null && meal.getName().toLowerCase().contains(lowerQuery)) {
                results.add(meal);
            } else if (meal.getCategory() != null && meal.getCategory().toLowerCase().contains(lowerQuery)) {
                results.add(meal);
            } else if (meal.getArea() != null && meal.getArea().toLowerCase().contains(lowerQuery)) {
                results.add(meal);
            }
        }

        view.hideLoading();

        if (results.isEmpty()) {
            view.showEmptyState(query);
        } else {
            view.showMeals(results);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}

