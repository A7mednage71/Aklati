package com.example.aklati.presentation.meal_details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.aklati.R;
import com.example.aklati.data.models.Meal;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {

    public static final String ARG_MEAL = "meal";

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
    private Meal currentMeal;

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

        // Get meal from arguments
        if (getArguments() != null) {
            currentMeal = (Meal) getArguments().getSerializable(ARG_MEAL);
        }

        // Bind views
        ivMealDetailImage = view.findViewById(R.id.ivMealDetailImage);
        tvDetailMealName = view.findViewById(R.id.tvDetailMealName);
        tvDetailArea = view.findViewById(R.id.tvDetailArea);
        chipCategory = view.findViewById(R.id.chipCategory);
        llIngredients = view.findViewById(R.id.llIngredients);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        llVideoSection = view.findViewById(R.id.llVideoSection);
        webViewVideo = view.findViewById(R.id.webViewVideo);
        fabFavorite = view.findViewById(R.id.fabFavorite);

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

        // Favourite toggle
        fabFavorite.setOnClickListener(v -> {
            if (presenter != null) presenter.toggleFavorite(currentMeal);
        });

        // Presenter
        presenter = new MealDetailsPresenter(this);
        presenter.loadMealDetails(currentMeal);
    }

    // ── MealDetailsContract.View ─────────────────────────────────────────────

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void showMealDetails(Meal meal) {
        this.currentMeal = meal;

        // Name
        tvDetailMealName.setText(meal.getName());

        // Area / country
        String area = meal.getArea();
        if (area != null && !area.isEmpty()) {
            tvDetailArea.setText(getString(R.string.area_cuisine_format, area));
        } else {
            tvDetailArea.setVisibility(View.GONE);
        }

        // Category chip
        String category = meal.getCategory();
        if (category != null && !category.isEmpty()) {
            chipCategory.setText(category);
        } else {
            chipCategory.setVisibility(View.GONE);
        }

        // Hero image
        if (meal.getImage() != null && !meal.getImage().isEmpty()) {
            Glide.with(requireContext())
                    .load(meal.getImage())
                    .placeholder(R.drawable.aklati_logo)
                    .centerCrop()
                    .into(ivMealDetailImage);
        } else {
            ivMealDetailImage.setImageResource(R.drawable.aklati_logo);
        }

        // Ingredients
        String[] ingredientArr = meal.getIngredients();
        String[] measureArr = meal.getMeasures();
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
        String instructions = meal.getInstructions();
        if (instructions != null && !instructions.isEmpty()) {
            tvInstructions.setText(instructions);
        } else {
            tvInstructions.setText(getString(R.string.no_instructions));
        }

        // YouTube video
        String ytUrl = meal.getYoutubeUrl();
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
        // no-op for now
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    @SuppressLint("SetJavaScriptEnabled")
    private void loadYouTubeVideo(String youtubeUrl) {
        // Extract video ID from URL (supports watch?v=XXXX and youtu.be/XXXX)
        String videoId = extractYoutubeId(youtubeUrl);
        if (videoId == null || videoId.isEmpty()) return;

        String embedHtml =
                "<html><body style='margin:0;padding:0;background:#000;'>" +
                        "<iframe width='100%' height='100%' " +
                        "src='https://www.youtube.com/embed/" + videoId + "' " +
                        "frameborder='0' allowfullscreen></iframe>" +
                        "</body></html>";

        WebSettings settings = webViewVideo.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        webViewVideo.setWebChromeClient(new WebChromeClient());
        webViewVideo.loadData(embedHtml, "text/html", "utf-8");
    }

    private String extractYoutubeId(String url) {
        if (url == null) return null;
        // https://www.youtube.com/watch?v=VIDEO_ID
        if (url.contains("v=")) {
            int start = url.indexOf("v=") + 2;
            int end = url.indexOf("&", start);
            return (end == -1) ? url.substring(start) : url.substring(start, end);
        }
        // https://youtu.be/VIDEO_ID
        if (url.contains("youtu.be/")) {
            int start = url.lastIndexOf("/") + 1;
            return url.substring(start);
        }
        return null;
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



