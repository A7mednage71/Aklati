package com.example.aklati.presentation.splash_screen;

import android.os.Handler;

public class SplashPresenter implements SplashContract.Presenter {
    private final SplashContract.View view;

    public SplashPresenter(SplashContract.View view) {
        this.view = view;
    }

    @Override
    public void startTimer() {
        new Handler().postDelayed(view::navigateToLogin, 2500);
    }
}