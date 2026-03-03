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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavorite(FavoriteMeal favoriteMeal);

    @Delete
    Completable deleteFavorite(FavoriteMeal favoriteMeal);

    @Query("SELECT * FROM user_favorites WHERE userId = :userId ORDER BY addedTimestamp DESC")
    Flowable<List<FavoriteMeal>> getFavoritesByUser(String userId);

    @Query("SELECT EXISTS(SELECT 1 FROM user_favorites WHERE userId = :userId AND mealId = :mealId)")
    Single<Boolean> isFavorite(String userId, String mealId);

    @Query("SELECT * FROM user_favorites WHERE userId = :userId AND mealId = :mealId")
    Single<FavoriteMeal> getFavoriteByUserAndMeal(String userId, String mealId);

    @Query("DELETE FROM user_favorites WHERE userId = :userId AND mealId = :mealId")
    Completable deleteFavoriteByUserAndMeal(String userId, String mealId);

    @Query("SELECT COUNT(*) FROM user_favorites WHERE userId = :userId")
    Single<Integer> getFavoritesCountByUser(String userId);
}

