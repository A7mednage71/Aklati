package com.example.aklati.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.aklati.data.local.entity.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {
    @Insert
    Completable insert(User user);

    @Query("SELECT * FROM users_table WHERE email = :email LIMIT 1")
    Single<User> getUserByEmail(String email);
}


// use Completable for insert because it doesn't return any data, it just indicates success or failure of the operation.
// use Single for getUserByEmail because it returns a single User object or an error if the user is not found.
// Single is appropriate for operations that emit either a single value or an error