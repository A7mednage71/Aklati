package com.example.aklati.presentation.register;

import com.example.aklati.data.local.dao.UserDao;
import com.example.aklati.data.local.entity.User;
import com.example.aklati.data.repository.UserRepository;
import com.example.aklati.utils.ValidationHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.View view;
    private final UserRepository userRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public RegisterPresenter(RegisterContract.View view, UserDao userDao) {
        this.view = view;
        this.userRepository = new UserRepository(userDao);
    }

    @Override
    public void register(String name, String email, String password, String confirmPassword) {
        ValidationHelper.ValidationResult result = ValidationHelper.validateRegister(name, email, password, confirmPassword);
        if (!result.isValid) {
            view.onRegisterError(result.message);
            return;
        }

        view.showLoading();

        User user = new User(name.trim(), email.trim(), password);

        disposables.add(userRepository.registerUser(user)
                .subscribeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    view.hideLoading();
                    view.onRegisterSuccess();
                }, throwable -> {
                    view.hideLoading();
                    String msg = throwable.getMessage() != null ? throwable.getMessage() : "Registration failed!";
                    view.onRegisterError(msg);
                }));
    }


    @Override
    public void dispose() {
        disposables.clear();
    }
}
