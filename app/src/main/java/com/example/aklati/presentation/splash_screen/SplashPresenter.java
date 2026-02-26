package com.example.aklati.presentation.splash_screen;

import android.os.Handler;

import com.example.aklati.data.local.prefs.SharedPrefsHelper;

public class SplashPresenter implements SplashContract.Presenter {
    private final SplashContract.View view;
    private final SharedPrefsHelper sharedPrefsHelper;

    public SplashPresenter(SplashContract.View view, SharedPrefsHelper sharedPrefsHelper) {
        this.sharedPrefsHelper = sharedPrefsHelper;
        this.view = view;
    }

    @Override
    public void checkLoginStatus() {
        new Handler().postDelayed(() -> {
            boolean isLoggedIn = sharedPrefsHelper.isLoggedIn();
            if (isLoggedIn) {
                view.navigateToHome();
            } else {
                view.navigateToLogin();
            }
        }, 3000);
    }
}