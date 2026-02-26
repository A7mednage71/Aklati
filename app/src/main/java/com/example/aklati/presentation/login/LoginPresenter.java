package com.example.aklati.presentation.login;

import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.repository.UserRepository;
import com.example.aklati.utils.ValidationHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View view;
    private final UserRepository repository;
    private final SharedPrefsHelper sharedPrefsHelper;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public LoginPresenter(LoginContract.View view, UserRepository repository, SharedPrefsHelper sharedPrefsHelper) {
        this.view = view;
        this.repository = repository;
        this.sharedPrefsHelper = sharedPrefsHelper;
    }

    @Override
    public void login(String email, String password) {
        ValidationHelper.ValidationResult result = ValidationHelper.validateLogin(email, password);
        if (!result.isValid) {
            view.onLoginError(result.message);
            return;
        }

        view.showLoading();
        disposable.add(repository.loginUser(email.trim(), password.trim())
                .subscribeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    sharedPrefsHelper.setLoggedIn(true);
                    sharedPrefsHelper.saveUserData(user.getName(), user.getEmail());
                    view.hideLoading();
                    view.onLoginSuccess(user);
                    view.navigateToHome();
                }, throwable -> {
                    view.hideLoading();
                    view.onLoginError("Login failed: " + throwable.getMessage());
                }));
    }

    @Override
    public void dispose() {
        disposable.clear();
    }
}
