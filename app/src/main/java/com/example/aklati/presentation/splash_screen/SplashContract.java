package com.example.aklati.presentation.splash_screen;

public interface SplashContract {
    interface View {
        void navigateToLogin();

        void navigateToHome();
    }

    interface Presenter {
        void startTimer();
    }
}