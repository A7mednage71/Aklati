package com.example.aklati.presentation.meal_details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.aklati.R;
import com.example.aklati.data.local.db.AppDatabase;
import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.models.MealDetails;
import com.example.aklati.data.remote.network.RetrofitClient;
import com.example.aklati.data.repository.FavoriteRepository;
import com.example.aklati.data.repository.MealRepository;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {

    public static final String ARG_MEAL_ID = "mealId";
    private static final String TAG = "MealDetailsFragment";
    private View scrollContent;
    private ShimmerFrameLayout layoutLoading;
    private ImageView ivMealDetailImage;
    private TextView tvDetailMealName;
    private TextView tvDetailArea;
    private Chip chipCategory;
    private LinearLayout llIngredients;
    private TextView tvInstructions;
    private LinearLayout llVideoSection;
    private WebView webViewVideo;
    private FloatingActionButton fabFavorite;

    private MealDetailsPresenter presenter;
    private MealDetails currentMealDetails;
    private String mealId;
    private View layoutError;
    private TextView tvErrorTitle;
    private View btnRetry;

    public MealDetailsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get meal ID from arguments
        if (getArguments() != null) {
            mealId = getArguments().getString(ARG_MEAL_ID);
        }

        // Bind views
        scrollContent = view.findViewById(R.id.scrollContent);
        layoutLoading = view.findViewById(R.id.layoutLoading);
        ivMealDetailImage = view.findViewById(R.id.ivMealDetailImage);
        tvDetailMealName = view.findViewById(R.id.tvDetailMealName);
        tvDetailArea = view.findViewById(R.id.tvDetailArea);
        chipCategory = view.findViewById(R.id.chipCategory);
        llIngredients = view.findViewById(R.id.llIngredients);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        llVideoSection = view.findViewById(R.id.llVideoSection);
        webViewVideo = view.findViewById(R.id.webViewVideo);
        fabFavorite = view.findViewById(R.id.fabFavorite);
        layoutError = view.findViewById(R.id.layoutError);
        tvErrorTitle = view.findViewById(R.id.tvErrorTitle);
        btnRetry = view.findViewById(R.id.btnRetry);

        // Setup WebView for YouTube video
        setupWebView();

        // Back
        View btnBack = view.findViewById(R.id.btnDetailBack);

        // Apply status bar inset so button isn't hidden behind system bar
        ViewCompat.setOnApplyWindowInsetsListener(btnBack, (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            lp.topMargin = statusBarHeight + 16;
            v.setLayoutParams(lp);
            return insets;
        });

        btnBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(MealDetailsFragment.this).popBackStack();
        });

        // Retry button
        btnRetry.setOnClickListener(v -> {
            if (mealId != null && !mealId.isEmpty()) {
                layoutError.setVisibility(View.GONE);
                presenter.loadMealDetails(mealId);
            }
        });

        // Favourite toggle
        fabFavorite.setOnClickListener(v -> {
            if (presenter != null && currentMealDetails != null) {
                presenter.toggleFavorite(currentMealDetails);
            }
        });

        // Create repositories
        MealRepository mealRepo = new MealRepository(RetrofitClient.getService());
        AppDatabase database = AppDatabase.getInstance(requireContext());

        // Get current user ID
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(requireContext());
        String userId = prefsHelper.getCurrentUserId();

        Log.d(TAG, "User ID from SharedPrefs: " + userId);

        FavoriteRepository favoriteRepo = new FavoriteRepository(database.favoriteMealDao(), userId);

        // Create Presenter with both repositories
        presenter = new MealDetailsPresenter(this, mealRepo, favoriteRepo);

        // Load meal details
        if (mealId != null && !mealId.isEmpty()) {
            view.postDelayed(() -> {
                if (presenter != null) {
                    presenter.loadMealDetails(mealId);
                }
            }, 300);
        } else {
            showErrorMessage("Invalid meal ID");
        }
    }

    @Override
    public void showLoading() {
        if (layoutError != null) {
            layoutError.setVisibility(View.GONE);
        }
        if (layoutLoading != null) {
            layoutLoading.setVisibility(View.VISIBLE);
            layoutLoading.startShimmer();
        }
        if (scrollContent != null) {
            scrollContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoading() {
        if (layoutLoading != null) {
            layoutLoading.stopShimmer();
            layoutLoading.setVisibility(View.GONE);
        }
        if (scrollContent != null) {
            scrollContent.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void showMealDetails(MealDetails mealDetails) {
        this.currentMealDetails = mealDetails;

        // Name
        tvDetailMealName.setText(mealDetails.getName());

        // Area / country
        String area = mealDetails.getArea();
        if (area != null && !area.isEmpty()) {
            tvDetailArea.setText(getString(R.string.area_cuisine_format, area));
        } else {
            tvDetailArea.setVisibility(View.GONE);
        }

        // Category chip
        String category = mealDetails.getCategory();
        if (category != null && !category.isEmpty()) {
            chipCategory.setText(category);
        } else {
            chipCategory.setVisibility(View.GONE);
        }

        // Hero image
        if (mealDetails.getImage() != null && !mealDetails.getImage().isEmpty()) {
            Glide.with(requireContext())
                    .load(mealDetails.getImage())
                    .placeholder(R.drawable.aklati_logo)
                    .centerCrop()
                    .into(ivMealDetailImage);
        } else {
            ivMealDetailImage.setImageResource(R.drawable.aklati_logo);
        }

        // Ingredients
        String[] ingredientArr = mealDetails.getIngredients();
        String[] measureArr = mealDetails.getMeasures();
        List<String> ingredientList = new ArrayList<>();
        List<String> measureList = new ArrayList<>();

        for (int i = 0; i < ingredientArr.length; i++) {
            String ing = ingredientArr[i];
            if (ing != null && !ing.trim().isEmpty()) {
                ingredientList.add(ing.trim());
                measureList.add(measureArr[i] != null ? measureArr[i] : "");
            }
        }
        IngredientAdapter.populate(requireContext(), llIngredients, ingredientList, measureList);

        // Instructions
        String instructions = mealDetails.getInstructions();
        if (instructions != null && !instructions.isEmpty()) {
            tvInstructions.setText(instructions);
        } else {
            tvInstructions.setText(getString(R.string.no_instructions));
        }

        // YouTube video
        String ytUrl = mealDetails.getYoutubeUrl();
        if (ytUrl != null && !ytUrl.isEmpty()) {
            llVideoSection.setVisibility(View.VISIBLE);
            loadYouTubeVideo(ytUrl);
        } else {
            llVideoSection.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateFavoriteIcon(boolean isFavorite) {
        if (fabFavorite == null) return;
        if (isFavorite) {
            fabFavorite.setImageResource(R.drawable.ic_favorite_filled);
            fabFavorite.setColorFilter(
                    requireContext().getColor(android.R.color.holo_red_light));
        } else {
            fabFavorite.setImageResource(R.drawable.ic_favorite);
            fabFavorite.clearColorFilter();
        }
    }

    @Override
    public void showErrorMessage(String error) {
        hideLoading();
        if (scrollContent != null) {
            scrollContent.setVisibility(View.GONE);
        }
        layoutError.setVisibility(View.VISIBLE);
        tvErrorTitle.setText(R.string.something_went_wrong);
    }

    @Override
    public void showMessage(String message) {
        if (getView() != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = webViewVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        // Custom User Agent for better compatibility
        webSettings.setUserAgentString(
                "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 " +
                        "Chrome/120.0.0.0 Mobile Safari/537.36"
        );

        webViewVideo.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webViewVideo.setWebChromeClient(new WebChromeClient());

        webViewVideo.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(
                    WebView view,
                    WebResourceRequest request,
                    WebResourceError error
            ) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, "WebView error, fallback to YouTube app");
                openYoutubeExternally();
            }
        });
    }

    private void loadYouTubeVideo(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) {
            llVideoSection.setVisibility(View.GONE);
            return;
        }

        String videoId = extractVideoId(youtubeUrl);
        if (videoId.isEmpty()) {
            llVideoSection.setVisibility(View.GONE);
            return;
        }

        llVideoSection.setVisibility(View.VISIBLE);

        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "<style>" +
                        "html, body { margin: 0; padding: 0; background: black; width: 100%; height: 100%; }" +
                        "iframe { width: 100%; height: 100%; border: 0; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<iframe src='https://www.youtube-nocookie.com/embed/" + videoId +
                        "?playsinline=1&rel=0' " +
                        "allow='accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share' " +
                        "allowfullscreen></iframe>" +
                        "</body>" +
                        "</html>";

        webViewVideo.loadDataWithBaseURL(
                "https://www.youtube-nocookie.com",
                html,
                "text/html",
                "UTF-8",
                null
        );
    }

    private String extractVideoId(String url) {
        try {
            Uri uri = Uri.parse(url);

            // https://www.youtube.com/watch?v=VIDEO_ID
            if (uri.getQueryParameter("v") != null) {
                return uri.getQueryParameter("v");
            }

            // https://youtu.be/VIDEO_ID
            if (url.contains("youtu.be/")) {
                return uri.getLastPathSegment();
            }
        } catch (Exception e) {
            Log.e(TAG, "Video ID extraction failed", e);
        }
        return "";
    }

    private void openYoutubeExternally() {
        if (currentMealDetails != null && currentMealDetails.getYoutubeUrl() != null) {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(currentMealDetails.getYoutubeUrl())
            );
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webViewVideo != null) {
            webViewVideo.onPause();
            webViewVideo.pauseTimers();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webViewVideo != null) {
            webViewVideo.onResume();
            webViewVideo.resumeTimers();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webViewVideo != null) {
            webViewVideo.stopLoading();
            webViewVideo.destroy();
        }
        if (presenter != null) presenter.detachView();
    }
}



