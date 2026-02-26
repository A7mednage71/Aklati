package com.example.aklati.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Junction table to link Users with their favorite Meals
 * Composite primary key: (userId, mealId)
 * This allows many-to-many relationship - same meal can be favorite for multiple users
 */
@Entity(
        tableName = "user_favorites",
        primaryKeys = {"userId", "mealId"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("userId"), @Index("mealId")}
)
public class FavoriteMeal {

    @NonNull
    private String userId;

    @NonNull
    private String mealId;

    private String mealName;
    private String mealImage;
    private String mealCategory;
    private String mealArea;
    private long addedTimestamp;

    public FavoriteMeal(@NonNull String userId, @NonNull String mealId,
                        String mealName, String mealImage,
                        String mealCategory, String mealArea) {
        this.userId = userId;
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealImage = mealImage;
        this.mealCategory = mealCategory;
        this.mealArea = mealArea;
        this.addedTimestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getMealId() {
        return mealId;
    }

    public void setMealId(@NonNull String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealImage() {
        return mealImage;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }

    public String getMealCategory() {
        return mealCategory;
    }

    public void setMealCategory(String mealCategory) {
        this.mealCategory = mealCategory;
    }

    public String getMealArea() {
        return mealArea;
    }

    public void setMealArea(String mealArea) {
        this.mealArea = mealArea;
    }

    public long getAddedTimestamp() {
        return addedTimestamp;
    }

    public void setAddedTimestamp(long addedTimestamp) {
        this.addedTimestamp = addedTimestamp;
    }
}
