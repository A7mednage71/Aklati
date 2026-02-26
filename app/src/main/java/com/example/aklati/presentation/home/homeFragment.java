package com.example.aklati.presentation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aklati.R;
import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.models.Category;
import com.example.aklati.data.models.MealDetails;
import com.example.aklati.data.remote.network.RetrofitClient;
import com.example.aklati.data.repository.MealRepository;
import com.example.aklati.presentation.category_meals.CategoryMealsFragment;
import com.example.aklati.presentation.meal_details.MealDetailsFragment;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class homeFragment extends Fragment implements HomeContract.View {
    private HomeContract.Presenter presenter;
    private RecyclerView rvCategories;
    private EditText etSearch;
    private ImageView ivRandomMeal;
    private TextView tvRandomMealName;
    private MaterialCardView cardRandomMeal;
    private MealDetails currentRandomMealDetails;
    // Loading state views
    private ShimmerFrameLayout shimmerLayout;
    private View contentScrollView;
    // Error handling views
    private View layoutError;
    private TextView tvErrorTitle;
    private Button btnRetry;
    // User data views
    private TextView tvWelcomeGreeting;

    public homeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvWelcomeGreeting = view.findViewById(R.id.tvWelcomeGreeting);
        rvCategories = view.findViewById(R.id.rvCategories);
        etSearch = view.findViewById(R.id.etSearch);
        ivRandomMeal = view.findViewById(R.id.ivRandomMeal);
        tvRandomMealName = view.findViewById(R.id.tvRandomMealName);
        cardRandomMeal = view.findViewById(R.id.cardRandomMeal);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        contentScrollView = view.findViewById(R.id.contentScrollView);
        layoutError = view.findViewById(R.id.layoutError);
        tvErrorTitle = view.findViewById(R.id.tvErrorTitle);
        btnRetry = view.findViewById(R.id.btnRetry);

        rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setNestedScrollingEnabled(false);

        etSearch.setOnClickListener(v -> navigateToSearch());

        cardRandomMeal.setOnClickListener(v -> {
            if (currentRandomMealDetails != null) navigateToDetails(currentRandomMealDetails);
        });

        btnRetry.setOnClickListener(v -> {
            layoutError.setVisibility(View.GONE);
            presenter.getRandomMeal();
            presenter.getCategories();
        });

        MealRepository repo = new MealRepository(RetrofitClient.getService());
        presenter = new HomePresenter(this, repo, SharedPrefsHelper.getInstance(requireContext()));

        view.postDelayed(() -> {
            if (presenter != null) {
                presenter.getUserName();
                presenter.getRandomMeal();
                presenter.getCategories();
            }
        }, 300);
    }

    @Override
    public void showLoading() {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(1200)
                .setBaseAlpha(0.7f)
                .setHighlightAlpha(1.0f)
                .setWidthRatio(1.5f)
                .build();
        shimmerLayout.setShimmer(shimmer);
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        contentScrollView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        contentScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRandomMeal(MealDetails meal) {
        currentRandomMealDetails = meal;
        tvRandomMealName.setText(meal.getName());
        Glide.with(this)
                .load(meal.getImage())
                .placeholder(R.drawable.aklati_logo)
                .centerCrop()
                .into(ivRandomMeal);
    }

    @Override
    public void showCategories(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(categories, category -> {
            Bundle args = new Bundle();
            args.putString(CategoryMealsFragment.ARG_CATEGORY_NAME, category.getName());
            args.putString(CategoryMealsFragment.ARG_CATEGORY_IMAGE, category.getImage());
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_home_to_categoryMeals, args);
        });
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void showUserName(String name) {
        if (name != null && !name.isEmpty()) {
            tvWelcomeGreeting.setText("Hi, " + name.split(" ")[0] + "!");
        } else {
            tvWelcomeGreeting.setText("Hi, Foodie!");
        }
    }

    @Override
    public void showError(String error) {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        contentScrollView.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvErrorTitle.setText(R.string.something_went_wrong);
    }

    @Override
    public void navigateToDetails(MealDetails mealDetails) {
        Bundle args = new Bundle();
        args.putString(MealDetailsFragment.ARG_MEAL_ID, mealDetails.getId());
        Navigation.findNavController(requireView()).navigate(R.id.action_home_to_mealDetails, args);
    }

    @Override
    public void navigateToSearch() {
        Navigation.findNavController(requireView()).navigate(R.id.searchFragment);
    }

}