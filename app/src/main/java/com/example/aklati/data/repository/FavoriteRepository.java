package com.example.aklati.data.repository;

import com.example.aklati.data.local.dao.FavoriteMealDao;
import com.example.aklati.data.local.entity.FavoriteMeal;
import com.example.aklati.data.local.mapper.FavoriteMealMapper;
import com.example.aklati.data.models.MealDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

/**
 * Repository for managing favorite meals using Room Database
 * Each user has their own separate favorites
 */
public class FavoriteRepository {

    private final FavoriteMealDao favoriteMealDao;
    private final String currentUserId;

    public FavoriteRepository(FavoriteMealDao favoriteMealDao, String userId) {
        this.favoriteMealDao = favoriteMealDao;
        this.currentUserId = userId;
    }

    /**
     * Add a meal to favorites for current user
     */
    public Completable addFavorite(MealDetails mealDetails) {
        if (currentUserId == null) {
            return Completable.error(new IllegalStateException("User not logged in"));
        }
        FavoriteMeal favoriteMeal = FavoriteMealMapper.toFavoriteMeal(currentUserId, mealDetails);
        return favoriteMealDao.insertFavorite(favoriteMeal);
    }

    /**
     * Remove a meal from favorites for current user by meal ID
     */
    public Completable removeFavoriteById(String mealId) {
        if (currentUserId == null) {
            return Completable.error(new IllegalStateException("User not logged in"));
        }
        return favoriteMealDao.deleteFavoriteByUserAndMeal(currentUserId, mealId);
    }

    /**
     * Get all favorite meals for current user
     * Flowable auto-updates when data changes
     */
    public Flowable<List<FavoriteMeal>> getAllFavorites() {
        if (currentUserId == null) {
            return Flowable.error(new IllegalStateException("User not logged in"));
        }
        return favoriteMealDao.getFavoritesByUser(currentUserId);
    }

    /**
     * Check if a meal is favorite for current user
     */
    public Single<Boolean> isFavorite(String mealId) {
        if (currentUserId == null) {
            return Single.just(false);
        }
        return favoriteMealDao.isFavorite(currentUserId, mealId);
    }

    /**
     * Get favorite meal by ID for current user
     */
    public Single<FavoriteMeal> getFavoriteById(String mealId) {
        if (currentUserId == null) {
            return Single.error(new IllegalStateException("User not logged in"));
        }
        return favoriteMealDao.getFavoriteByUserAndMeal(currentUserId, mealId);
    }

    /**
     * Get count of all favorites for current user
     */
    public Single<Integer> getFavoritesCount() {
        if (currentUserId == null) {
            return Single.just(0);
        }
        return favoriteMealDao.getFavoritesCountByUser(currentUserId);
    }
}
