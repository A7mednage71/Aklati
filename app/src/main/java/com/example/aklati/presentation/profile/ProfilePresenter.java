package com.example.aklati.presentation.profile;

import android.util.Log;

import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.repository.FavoriteRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfilePresenter implements ProfileContract.Presenter {

    private static final String TAG = "ProfilePresenter";
    private final SharedPrefsHelper sharedPrefsHelper;
    private final FavoriteRepository favoriteRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private ProfileContract.View view;

    public ProfilePresenter(ProfileContract.View view, SharedPrefsHelper sharedPrefsHelper,
                            FavoriteRepository favoriteRepository) {
        this.sharedPrefsHelper = sharedPrefsHelper;
        this.favoriteRepository = favoriteRepository;
        this.view = view;
    }

    @Override
    public void loadProfile() {
        if (view == null) return;

        // Load user info from SharedPrefs
        String name = sharedPrefsHelper.getData(SharedPrefsHelper.KEY_USER_NAME);
        String email = sharedPrefsHelper.getData(SharedPrefsHelper.KEY_USER_EMAIL);
        view.showUserInfo(name, email);

        // Load favorites count from database
        loadFavoritesCount();
    }

    @Override
    public void loadFavoritesCount() {
        disposables.add(favoriteRepository.getFavoritesCount().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(count -> {
            if (view != null) {
                view.showStats(count, 0);
                Log.d(TAG, "Favorites count loaded: " + count);
            }
        }, throwable -> {
            Log.e(TAG, "Error loading favorites count", throwable);
            if (view != null) {
                // Show 0 if error occurs
                view.showStats(0, 0);
            }
        }));
    }

    @Override
    public void logout() {
        if (view == null) return;
        view.navigateToLogin();
    }

    @Override
    public void detachView() {
        view = null;
        disposables.clear();
    }
}

