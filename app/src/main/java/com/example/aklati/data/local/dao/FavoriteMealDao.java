package com.example.aklati.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.aklati.data.local.entity.FavoriteMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteMealDao {

    /**
     * Insert a meal to favorites for specific user
     * If meal already exists for this user, it will be replaced
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavorite(FavoriteMeal favoriteMeal);

    /**
     * Delete a meal from favorites
     */
    @Delete
    Completable deleteFavorite(FavoriteMeal favoriteMeal);

    /**
     * Get all favorite meals for specific user ordered by added timestamp (newest first)
     */
    @Query("SELECT * FROM user_favorites WHERE userId = :userId ORDER BY addedTimestamp DESC")
    Flowable<List<FavoriteMeal>> getFavoritesByUser(String userId);

    /**
     * Check if a meal is favorite for specific user
     */
    @Query("SELECT EXISTS(SELECT 1 FROM user_favorites WHERE userId = :userId AND mealId = :mealId)")
    Single<Boolean> isFavorite(String userId, String mealId);

    /**
     * Get a single favorite meal by user ID and meal ID
     */
    @Query("SELECT * FROM user_favorites WHERE userId = :userId AND mealId = :mealId")
    Single<FavoriteMeal> getFavoriteByUserAndMeal(String userId, String mealId);

    /**
     * Delete favorite by user ID and meal ID
     */
    @Query("DELETE FROM user_favorites WHERE userId = :userId AND mealId = :mealId")
    Completable deleteFavoriteByUserAndMeal(String userId, String mealId);

    /**
     * Get count of all favorites for specific user
     */
    @Query("SELECT COUNT(*) FROM user_favorites WHERE userId = :userId")
    Single<Integer> getFavoritesCountByUser(String userId);
}

