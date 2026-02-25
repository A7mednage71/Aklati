package com.example.aklati.presentation.profile;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void loadProfile() {
        if (view == null) return;
        // Dummy user data (replace with real auth/shared prefs later)
        view.showUserInfo("Ahmed Mohamed", "ahmed@example.com");
        view.showStats(6, 8);
    }

    @Override
    public void logout() {
        if (view == null) return;
        view.navigateToLogin();
    }

    @Override
    public void detachView() {
        view = null;
    }
}

