package com.example.aklati.presentation.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.aklati.R;
import com.example.aklati.data.local.db.AppDatabase;
import com.example.aklati.data.local.entity.User;
import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.repository.UserRepository;
import com.example.aklati.presentation.common.LoadingDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter presenter;
    private TextInputEditText etEmail, etPassword;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPrefsHelper helper = SharedPrefsHelper.getInstance(requireContext());
        UserRepository repo = new UserRepository(AppDatabase.getInstance(requireContext()).userDao());

        presenter = new LoginPresenter(this, repo, helper);
        loadingDialog = new LoadingDialog(requireContext());

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        MaterialButton btnLogin = view.findViewById(R.id.btnLogin);
        View loginRootView = view.findViewById(R.id.loginRootView);

        ViewCompat.setOnApplyWindowInsetsListener(loginRootView, (v, insets) -> {
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), imeInsets.bottom + systemBars.bottom);
            return insets;
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
            presenter.login(email, password);
        });

        view.findViewById(R.id.tvRegisterLink).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_login_to_register));
    }

    @Override
    public void showLoading() {
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void onLoginSuccess(User user) {
        Toast.makeText(requireContext(), "Login successful..!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Navigation.findNavController(requireView()).navigate(R.id.action_login_to_home);
    }

    @Override
    public void clearInputs() {
        etEmail.setText("");
        etPassword.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.dispose();
        if (loadingDialog != null) loadingDialog.dismiss();
    }
}
