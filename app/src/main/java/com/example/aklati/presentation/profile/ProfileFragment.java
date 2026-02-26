package com.example.aklati.presentation.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.aklati.R;
import com.example.aklati.data.local.db.AppDatabase;
import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.repository.FavoriteRepository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileFragment extends Fragment implements ProfileContract.View {

    private TextView tvUserName;
    private TextView tvUserEmail;
    private TextView tvInfoName;
    private TextView tvInfoEmail;
    private TextView tvFavoritesCount;
    private View btnLogout;

    private ProfilePresenter presenter;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvInfoName = view.findViewById(R.id.tvInfoName);
        tvInfoEmail = view.findViewById(R.id.tvInfoEmail);
        tvFavoritesCount = view.findViewById(R.id.tvFavoritesCount);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Get SharedPrefs and userId
        SharedPrefsHelper helper = SharedPrefsHelper.getInstance(requireContext());
        String userId = helper.getCurrentUserId();

        // Create FavoriteRepository
        AppDatabase database = AppDatabase.getInstance(requireContext());
        FavoriteRepository favoriteRepository = new FavoriteRepository(database.favoriteMealDao(), userId);

        // Create presenter with both dependencies
        presenter = new ProfilePresenter(this, helper, favoriteRepository);
        presenter.loadProfile();

        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.logout_confirm_title)).setMessage(getString(R.string.logout_confirm_message)).setPositiveButton(getString(R.string.logout), (dialog, which) -> presenter.logout()).setNegativeButton(getString(R.string.cancel), null).show();
    }

    @Override
    public void showUserInfo(String name, String email) {
        tvUserName.setText(name);
        tvUserEmail.setText(email);
        tvInfoName.setText(name);
        tvInfoEmail.setText(email);
    }

    @Override
    public void showStats(int favoritesCount, int categoriesCount) {
        tvFavoritesCount.setText(String.valueOf(favoritesCount));
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_profile_to_login);
        SharedPrefsHelper.getInstance(requireContext()).setLoggedIn(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.detachView();
    }
}
