package com.example.aklati.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Meal implements Serializable {
    @SerializedName("idMeal")
    private final String id;

    @SerializedName("strMeal")
    private final String name;

    @SerializedName("strMealThumb")
    private final String image;

    public Meal(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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
}
