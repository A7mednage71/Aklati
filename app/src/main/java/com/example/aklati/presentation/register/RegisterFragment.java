package com.example.aklati.presentation.register;

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
import com.example.aklati.presentation.common.LoadingDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment implements RegisterContract.View {

    private RegisterContract.Presenter presenter;
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new RegisterPresenter(this, AppDatabase.getInstance(requireContext()).userDao());
        loadingDialog = new LoadingDialog(requireContext());

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        MaterialButton btnRegister = view.findViewById(R.id.btnRegister);
        View registerRootView = view.findViewById(R.id.registerRootView);

        ViewCompat.setOnApplyWindowInsetsListener(registerRootView, (v, insets) -> {
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), imeInsets.bottom);
            return insets;
        });

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
            String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";
            presenter.register(name, email, password, confirmPassword);
        });

        view.findViewById(R.id.tvLoginLink).setOnClickListener(v -> navigateToLogin());
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
    public void onRegisterSuccess() {
        Toast.makeText(requireContext(), "Registration successful..!", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }

    @Override
    public void onRegisterError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearInputs() {
        etName.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_register_to_login);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.dispose();
        if (loadingDialog != null) loadingDialog.dismiss();
    }
}
