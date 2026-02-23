package com.example.aklati.presentation.category_meals;

import com.example.aklati.data.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class CategoryMealsPresenter implements CategoryMealsContract.Presenter {

    private CategoryMealsContract.View view;

    public CategoryMealsPresenter(CategoryMealsContract.View view) {
        this.view = view;
    }

    @Override
    public void getMealsByCategory(String categoryName) {
        if (view == null) return;

        view.showLoading();

        // Dummy data filtered by category name
        List<Meal> filtered = getDummyMealsByCategory(categoryName);

        view.hideLoading();

        if (filtered.isEmpty()) {
            view.showEmptyState();
        } else {
            view.showMeals(filtered);
        }
    }

    private List<Meal> getDummyMealsByCategory(String categoryName) {
        List<Meal> allMeals = new ArrayList<>();

        // Beef
        allMeals.add(new Meal("1", "Beef Wellington", null, "British", "Beef"));
        allMeals.add(new Meal("2", "Beef Stroganoff", null, "Russian", "Beef"));
        allMeals.add(new Meal("3", "Beef Rendang", null, "Malaysian", "Beef"));
        allMeals.add(new Meal("4", "Beef Tacos", null, "Mexican", "Beef"));
        allMeals.add(new Meal("1", "Beef Wellington", null, "British", "Beef"));
        allMeals.add(new Meal("2", "Beef Stroganoff", null, "Russian", "Beef"));
        allMeals.add(new Meal("3", "Beef Rendang", null, "Malaysian", "Beef"));
        allMeals.add(new Meal("4", "Beef Tacos", null, "Mexican", "Beef"));
        allMeals.add(new Meal("1", "Beef Wellington", null, "British", "Beef"));
        allMeals.add(new Meal("2", "Beef Stroganoff", null, "Russian", "Beef"));
        allMeals.add(new Meal("3", "Beef Rendang", null, "Malaysian", "Beef"));
        allMeals.add(new Meal("4", "Beef Tacos", null, "Mexican", "Beef"));


        // Chicken
        allMeals.add(new Meal("5", "Chicken Tikka Masala", null, "Indian", "Chicken"));
        allMeals.add(new Meal("6", "Grilled Chicken", null, "American", "Chicken"));
        allMeals.add(new Meal("7", "Chicken Alfredo", null, "Italian", "Chicken"));
        allMeals.add(new Meal("8", "Chicken Shawarma", null, "Lebanese", "Chicken"));

        // Dessert
        allMeals.add(new Meal("9", "Chocolate Lava Cake", null, "French", "Dessert"));
        allMeals.add(new Meal("10", "Crème Brûlée", null, "French", "Dessert"));
        allMeals.add(new Meal("11", "Tiramisu", null, "Italian", "Dessert"));
        allMeals.add(new Meal("12", "Baklava", null, "Turkish", "Dessert"));

        // Lamb
        allMeals.add(new Meal("13", "Lamb Chops", null, "Greek", "Lamb"));
        allMeals.add(new Meal("14", "Lamb Tagine", null, "Moroccan", "Lamb"));
        allMeals.add(new Meal("15", "Lamb Kofta", null, "Lebanese", "Lamb"));

        // Pasta
        allMeals.add(new Meal("16", "Spaghetti Bolognese", null, "Italian", "Pasta"));
        allMeals.add(new Meal("17", "Penne Arrabbiata", null, "Italian", "Pasta"));
        allMeals.add(new Meal("18", "Fettuccine Alfredo", null, "Italian", "Pasta"));
        allMeals.add(new Meal("19", "Lasagna", null, "Italian", "Pasta"));

        // Seafood
        allMeals.add(new Meal("20", "Grilled Salmon", null, "Japanese", "Seafood"));
        allMeals.add(new Meal("21", "Shrimp Scampi", null, "Italian", "Seafood"));
        allMeals.add(new Meal("22", "Fish and Chips", null, "British", "Seafood"));
        allMeals.add(new Meal("23", "Lobster Bisque", null, "French", "Seafood"));

        // Vegan
        allMeals.add(new Meal("24", "Vegan Buddha Bowl", null, "American", "Vegan"));
        allMeals.add(new Meal("25", "Falafel Wrap", null, "Lebanese", "Vegan"));
        allMeals.add(new Meal("26", "Vegan Curry", null, "Indian", "Vegan"));

        // Vegetarian
        allMeals.add(new Meal("27", "Caesar Salad", null, "American", "Vegetarian"));
        allMeals.add(new Meal("28", "Margherita Pizza", null, "Italian", "Vegetarian"));
        allMeals.add(new Meal("29", "Caprese Salad", null, "Italian", "Vegetarian"));
        allMeals.add(new Meal("30", "Stuffed Peppers", null, "Greek", "Vegetarian"));

        List<Meal> result = new ArrayList<>();
        for (Meal meal : allMeals) {
            if (meal.getCategory() != null &&
                    meal.getCategory().equalsIgnoreCase(categoryName)) {
                result.add(meal);
            }
        }
        return result;
    }

    @Override
    public void detachView() {
        view = null;
    }
}

