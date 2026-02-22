package com.example.aklati.presentation.login;

public interface LoginContract {
    interface View {
        void showLoading();

        void hideLoading();

        void onLoginSuccess();

        void onLoginError(String message);

        void navigateToHome();

        void clearInputs();
    }

    interface Presenter {
        void login(String email, String password);
    }
}