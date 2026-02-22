package com.example.aklati.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("idCategory")
    private String id;
    @SerializedName("strCategory")
    private String name;

    @SerializedName("strCategoryThumb")
    private String image;

    @SerializedName("strCategoryDescription")
    private String description;

    // for local/dummy data
    private int drawableResId;

    public Category() {
    }

    public Category(String id, String name, String image, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    // local constructor
    public Category(String name, int drawableResId) {
        this.name = name;
        this.drawableResId = drawableResId;
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

    public String getDescription() {
        return description;
    }

    public int getDrawableResId() {
        return drawableResId;
    }
}