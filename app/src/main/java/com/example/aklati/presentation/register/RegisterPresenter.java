package com.example.aklati.presentation.register;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.View view;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void register(String name, String email, String password, String confirmPassword) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            view.onRegisterError("the name is required..!");
            return;
        }

        if (email == null || email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.onRegisterError("email is not valid..!");
            return;
        }

        if (password == null || password.length() < 6) {
            view.onRegisterError("Password must be at least 6 characters..!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            view.onRegisterError("Passwords do not match..!");
            return;
        }

        view.showLoading();

        // TODO: Call the actual API/Repository here
        // For now simulate success
        view.hideLoading();
        view.onRegisterSuccess();
    }
}

