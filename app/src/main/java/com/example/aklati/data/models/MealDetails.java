package com.example.aklati.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MealDetails implements Serializable {

    @SerializedName("idMeal")
    private final String id;

    @SerializedName("strMeal")
    private final String name;

    @SerializedName("strMealThumb")
    private final String image;

    @SerializedName("strArea")
    private final String area;
    @SerializedName("strCategory")
    private final String category;
    @SerializedName("strInstructions")
    private String instructions;
    @SerializedName("strYoutube")
    private String youtubeUrl;

    // Ingredients 1-20
    @SerializedName("strIngredient1")
    private String ingredient1;
    @SerializedName("strIngredient2")
    private String ingredient2;
    @SerializedName("strIngredient3")
    private String ingredient3;
    @SerializedName("strIngredient4")
    private String ingredient4;
    @SerializedName("strIngredient5")
    private String ingredient5;
    @SerializedName("strIngredient6")
    private String ingredient6;
    @SerializedName("strIngredient7")
    private String ingredient7;
    @SerializedName("strIngredient8")
    private String ingredient8;
    @SerializedName("strIngredient9")
    private String ingredient9;
    @SerializedName("strIngredient10")
    private String ingredient10;
    @SerializedName("strIngredient11")
    private String ingredient11;
    @SerializedName("strIngredient12")
    private String ingredient12;
    @SerializedName("strIngredient13")
    private String ingredient13;
    @SerializedName("strIngredient14")
    private String ingredient14;
    @SerializedName("strIngredient15")
    private String ingredient15;
    @SerializedName("strIngredient16")
    private String ingredient16;
    @SerializedName("strIngredient17")
    private String ingredient17;
    @SerializedName("strIngredient18")
    private String ingredient18;
    @SerializedName("strIngredient19")
    private String ingredient19;
    @SerializedName("strIngredient20")
    private String ingredient20;

    // Measures 1-20
    @SerializedName("strMeasure1")
    private String measure1;
    @SerializedName("strMeasure2")
    private String measure2;
    @SerializedName("strMeasure3")
    private String measure3;
    @SerializedName("strMeasure4")
    private String measure4;
    @SerializedName("strMeasure5")
    private String measure5;
    @SerializedName("strMeasure6")
    private String measure6;
    @SerializedName("strMeasure7")
    private String measure7;
    @SerializedName("strMeasure8")
    private String measure8;
    @SerializedName("strMeasure9")
    private String measure9;
    @SerializedName("strMeasure10")
    private String measure10;
    @SerializedName("strMeasure11")
    private String measure11;
    @SerializedName("strMeasure12")
    private String measure12;
    @SerializedName("strMeasure13")
    private String measure13;
    @SerializedName("strMeasure14")
    private String measure14;
    @SerializedName("strMeasure15")
    private String measure15;
    @SerializedName("strMeasure16")
    private String measure16;
    @SerializedName("strMeasure17")
    private String measure17;
    @SerializedName("strMeasure18")
    private String measure18;
    @SerializedName("strMeasure19")
    private String measure19;
    @SerializedName("strMeasure20")
    private String measure20;

    // favorite flag (local only)
    private boolean isFavorite;

    public MealDetails(String id, String name, String image, String area, String category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.area = area;
        this.category = category;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public static List<MealDetails> dummyMeals() {
        List<MealDetails> list = new ArrayList<>();
        list.add(new MealDetails("1", "Spaghetti Bolognese", null, "Italian", "Pasta"));
        list.add(new MealDetails("2", "Chicken Tikka Masala", null, "Indian", "Chicken"));
        list.add(new MealDetails("3", "Beef Stew", null, "British", "Beef"));
        list.add(new MealDetails("4", "Caesar Salad", null, "American", "Vegetarian"));
        list.add(new MealDetails("5", "Grilled Salmon", null, "Japanese", "Seafood"));
        list.add(new MealDetails("6", "Chocolate Lava Cake", null, "French", "Dessert"));
        return list;
    }

    /**
     * Returns a fully-populated dummy meal for the Details screen.
     */
    public static MealDetails dummyDetailMeal() {
        MealDetails mealDetails = new MealDetails("52772", "Teriyaki Chicken Casserole",
                null, "Japanese", "Chicken");
        mealDetails.setYoutubeUrl("https://www.youtube.com/watch?v=4aZr5hZXP_s");
        mealDetails.setInstructions(
                "1. Preheat oven to 350° F. Spray a 9x13-inch baking pan with non-stick spray.\n\n" +
                        "2. Combine soy sauce, ½ cup water, brown sugar, ginger and garlic in a small saucepan.\n\n" +
                        "3. Cover over medium heat, stirring occasionally, until the sugar dissolves (about 5 minutes).\n\n" +
                        "4. Meanwhile, mix together the corn starch and 2 tablespoons of water in a separate dish until smooth.\n\n" +
                        "5. Once the sugar is dissolved, add the corn starch mixture to the saucepan. Stir to combine.\n\n" +
                        "6. Cook until the mixture thickens, stirring occasionally (about 5 more minutes).\n\n" +
                        "7. Place the chicken breasts in the prepared pan. Pour the teriyaki sauce evenly over the chicken.\n\n" +
                        "8. Bake for 20 minutes. Add the vegetables and stir to coat with sauce.\n\n" +
                        "9. Cover the pan with foil and bake for 30 more minutes. Serve over rice."
        );
        mealDetails.setIngredients(
                "soy sauce", "water", "brown sugar", "ground ginger", "minced garlic",
                "cornstarch", "chicken breasts", "broccoli", "carrots", "rice"
        );
        mealDetails.setMeasures(
                "3/4 cup", "1/2 cup", "1/4 cup", "1/2 tsp", "1/2 tsp",
                "4 tbs", "2", "2 cups", "1 cup", "2 cups"
        );
        return mealDetails;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getArea() {
        return area;
    }

    public String getCategory() {
        return category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public String[] getIngredients() {
        return new String[]{ingredient1, ingredient2, ingredient3, ingredient4, ingredient5,
                ingredient6, ingredient7, ingredient8, ingredient9, ingredient10,
                ingredient11, ingredient12, ingredient13, ingredient14, ingredient15,
                ingredient16, ingredient17, ingredient18, ingredient19, ingredient20};
    }

    public String[] getMeasures() {
        return new String[]{measure1, measure2, measure3, measure4, measure5,
                measure6, measure7, measure8, measure9, measure10,
                measure11, measure12, measure13, measure14, measure15,
                measure16, measure17, measure18, measure19, measure20};
    }

    // ── Dummy data ────────────────────────────────────────────────────────────

    public void setIngredients(String i1, String i2, String i3, String i4, String i5,
                               String i6, String i7, String i8, String i9, String i10) {
        ingredient1 = i1;
        ingredient2 = i2;
        ingredient3 = i3;
        ingredient4 = i4;
        ingredient5 = i5;
        ingredient6 = i6;
        ingredient7 = i7;
        ingredient8 = i8;
        ingredient9 = i9;
        ingredient10 = i10;
    }

    public void setMeasures(String m1, String m2, String m3, String m4, String m5,
                            String m6, String m7, String m8, String m9, String m10) {
        measure1 = m1;
        measure2 = m2;
        measure3 = m3;
        measure4 = m4;
        measure5 = m5;
        measure6 = m6;
        measure7 = m7;
        measure8 = m8;
        measure9 = m9;
        measure10 = m10;
    }
}

