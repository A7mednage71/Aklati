package com.example.aklati.data.repository;

import com.example.aklati.data.local.dao.FavoriteMealDao;
import com.example.aklati.data.local.entity.FavoriteMeal;
import com.example.aklati.data.local.mapper.FavoriteMealMapper;
import com.example.aklati.data.models.MealDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class FavoriteRepository {

    private final FavoriteMealDao favoriteMealDao;
    private final String currentUserId;

    public FavoriteRepository(FavoriteMealDao favoriteMealDao, String userId) {
        this.favoriteMealDao = favoriteMealDao;
        this.currentUserId = userId;
    }

    public Completable addFavorite(MealDetails mealDetails) {
        if (currentUserId == null) {
            return Completable.error(new IllegalStateException("User not logged in"));
        }
        FavoriteMeal favoriteMeal = FavoriteMealMapper.toFavoriteMeal(currentUserId, mealDetails);
        return favoriteMealDao.insertFavorite(favoriteMeal);
    }

    public Completable removeFavoriteById(String mealId) {
        if (currentUserId == null) {
            return Completable.error(new IllegalStateException("User not logged in"));
        }
        return favoriteMealDao.deleteFavoriteByUserAndMeal(currentUserId, mealId);
    }

    public Flowable<List<FavoriteMeal>> getAllFavorites() {
        if (currentUserId == null) {
            return Flowable.error(new IllegalStateException("User not logged in"));
        }
        return favoriteMealDao.getFavoritesByUser(currentUserId);
    }

    public Single<Boolean> isFavorite(String mealId) {
        if (currentUserId == null) {
            return Single.just(false);
        }
        return favoriteMealDao.isFavorite(currentUserId, mealId);
    }

    public Single<FavoriteMeal> getFavoriteById(String mealId) {
        if (currentUserId == null) {
            return Single.error(new IllegalStateException("User not logged in"));
        }
        return favoriteMealDao.getFavoriteByUserAndMeal(currentUserId, mealId);
    }

    public Single<Integer> getFavoritesCount() {
        if (currentUserId == null) {
            return Single.just(0);
        }
        return favoriteMealDao.getFavoritesCountByUser(currentUserId);
    }
}
