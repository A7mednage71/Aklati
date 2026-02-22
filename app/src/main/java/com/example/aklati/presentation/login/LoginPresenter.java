package com.example.aklati.presentation.login;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.onLoginError("Invalid email address..!");
            return;
        }
        if (password == null || password.length() < 6) {
            view.onLoginError("Password must be at least 6 characters..!");
            return;
        }

        view.showLoading();
        // TODO: Call API/Repository
        view.hideLoading();
        view.onLoginSuccess();
        view.navigateToHome();
    }
}
