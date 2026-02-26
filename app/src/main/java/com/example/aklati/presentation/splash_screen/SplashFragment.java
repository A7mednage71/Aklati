package com.example.aklati.presentation.splash_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.aklati.R;
import com.example.aklati.data.local.prefs.SharedPrefsHelper;

public class SplashFragment extends Fragment implements SplashContract.View {

    private SplashPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Animations
        ImageView imgLogo = view.findViewById(R.id.imgLogo);
        TextView tvWelcome = view.findViewById(R.id.tvWelcome);

        imgLogo.animate().alpha(1f).scaleX(1.5f).scaleY(1.5f).setDuration(1000)
                .withEndAction(() -> imgLogo.animate().scaleX(1f).scaleY(1f).setDuration(500).start())
                .start();

        tvWelcome.setAlpha(0f);
        tvWelcome.setTranslationY(400f);
        tvWelcome.setScaleX(0.2f);
        tvWelcome.setScaleY(0.2f);
        tvWelcome.animate().alpha(1f).translationY(0f).scaleX(1.0f).scaleY(1.0f)
                .setDuration(1200).setStartDelay(1000)
                .setInterpolator(new android.view.animation.AnticipateOvershootInterpolator(2.0f))
                .start();

        // Check login status
        SharedPrefsHelper helper = SharedPrefsHelper.getInstance(requireContext());
        presenter = new SplashPresenter(this, helper);
        presenter.checkLoginStatus();
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_splash_to_login);
    }

    @Override
    public void navigateToHome() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_splash_to_home);
    }
}

