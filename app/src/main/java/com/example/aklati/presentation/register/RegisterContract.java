package com.example.aklati.presentation.register;

public interface RegisterContract {

    interface View {
        void showLoading();

        void hideLoading();

        void onRegisterSuccess();

        void onRegisterError(String message);

        void clearInputs();

        void navigateToLogin();
    }

    interface Presenter {
        void register(String name, String email, String password, String confirmPassword);

        void dispose();
    }
}

