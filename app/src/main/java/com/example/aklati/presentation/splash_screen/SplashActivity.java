package com.example.aklati.presentation.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.aklati.MainActivity;
import com.example.aklati.R;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imgLogo = findViewById(R.id.imgLogo);
        TextView tvWelcome = findViewById(R.id.tvWelcome);

        imgLogo.animate().alpha(1f).scaleX(1.5f).scaleY(1.5f).setDuration(1000).withEndAction(() -> {
            imgLogo.animate().scaleX(1f).scaleY(1f).setDuration(500).start();
        }).start();

        tvWelcome.setAlpha(0f);
        tvWelcome.setTranslationY(400f);
        tvWelcome.setScaleX(0.2f);
        tvWelcome.setScaleY(0.2f);
        tvWelcome.animate().alpha(1f).translationY(0f).scaleX(1.0f).scaleY(1.0f).setDuration(1200).setStartDelay(1000)
                .setInterpolator(new android.view.animation.AnticipateOvershootInterpolator(2.0f)).start();

        presenter = new SplashPresenter(this);
        presenter.startTimer();
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();  // Close SplashActivity so it's removed from the back stack
    }

    @Override
    public void navigateToHome() {

    }
}