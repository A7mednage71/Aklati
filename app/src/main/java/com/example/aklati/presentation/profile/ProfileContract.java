package com.example.aklati.presentation.profile;

public interface ProfileContract {

    interface View {
        void showUserInfo(String name, String email);

        void showStats(int favoritesCount, int categoriesCount);

        void navigateToLogin();
    }

    interface Presenter {
        void loadProfile();

        void logout();

        void detachView();
    }
}

