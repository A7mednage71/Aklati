package com.example.aklati.presentation.splash_screen;

public interface SplashContract {
    interface View {
        // UI navigation
        void navigateToLogin();

        void navigateToHome();
    }

    interface Presenter {
        // Splash logic
        void startTimer();
    }
}