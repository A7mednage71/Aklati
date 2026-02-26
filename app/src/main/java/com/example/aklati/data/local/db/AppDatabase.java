package com.example.aklati.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.aklati.data.local.dao.FavoriteMealDao;
import com.example.aklati.data.local.dao.UserDao;
import com.example.aklati.data.local.entity.FavoriteMeal;
import com.example.aklati.data.local.entity.User;

@Database(entities = {User.class, FavoriteMeal.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "aklati_db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();

    public abstract FavoriteMealDao favoriteMealDao();
}