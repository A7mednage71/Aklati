package com.example.aklati.presentation.login;

import com.example.aklati.data.local.entity.User;

public interface LoginContract {
    interface View {
        void showLoading();

        void hideLoading();

        void onLoginSuccess(User user);

        void onLoginError(String message);

        void navigateToHome();

        void clearInputs();
    }

    interface Presenter {
        void login(String email, String password);

        // to clear any resources or references to the view to prevent memory leaks
        void dispose();
    }
}