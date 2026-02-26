package com.example.aklati.data.repository;

import com.example.aklati.data.local.dao.UserDao;
import com.example.aklati.data.local.entity.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public Completable registerUser(User user) {
        return userDao.getUserByEmail(user.getEmail())
                .flatMapCompletable(existingUser ->
                        Completable.error(new Exception("Email already exists, try another one!"))
                )
                .onErrorResumeNext(throwable -> userDao.insert(user));
    }


    public Single<User> loginUser(String email, String password) {
        return userDao.getUserByEmail(email)
                .flatMap(user -> {
                    if (user.getPassword().equals(password)) {
                        return Single.just(user);
                    } else {
                        return Single.error(new Exception("Password is incorrect, try again!"));
                    }
                })
                .onErrorResumeNext(throwable ->
                        Single.error(new Exception("Email not found, please register first!"))
                );
    }
    
}