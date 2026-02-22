package com.example.aklati.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meal implements Serializable {

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
    @SerializedName("strIngredient1")
    private String ingredient1;
    @SerializedName("strIngredient2")
    private String ingredient2;
    @SerializedName("strIngredient3")
    private String ingredient3;

    @SerializedName("strMeasure1")
    private String measure1;
    @SerializedName("strMeasure2")
    private String measure2;
    @SerializedName("strMeasure3")
    private String measure3;

    public Meal(String id, String name, String image, String area, String category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.area = area;
        this.category = category;
    }

    public static List<Meal> dummyMeals() {
        List<Meal> list = new ArrayList<>();
        list.add(new Meal("1", "Spaghetti Bolognese", null, "Italian", "Pasta"));
        list.add(new Meal("2", "Chicken Tikka Masala", null, "Indian", "Chicken"));
        list.add(new Meal("3", "Beef Stew", null, "British", "Beef"));
        list.add(new Meal("4", "Caesar Salad", null, "American", "Vegetarian"));
        list.add(new Meal("5", "Grilled Salmon", null, "Japanese", "Seafood"));
        list.add(new Meal("6", "Chocolate Lava Cake", null, "French", "Dessert"));
        return list;
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

    public String getInstructions() {
        return instructions;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public String getCategory() {
        return category;
    }
}