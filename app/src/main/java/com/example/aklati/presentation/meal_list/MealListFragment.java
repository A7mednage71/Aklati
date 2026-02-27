package com.example.aklati.presentation.meal_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aklati.R;
import com.example.aklati.data.models.Meal;
import com.example.aklati.data.remote.network.RetrofitClient;
import com.example.aklati.data.repository.MealRepository;
import com.example.aklati.presentation.meal_details.MealDetailsFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MealListFragment extends Fragment implements MealsListContract.View {

    public static final String ARG_IMAGE = "image";
    public static final String ARG_TITLE = "title";
    public static final String ARG_FILTER_TYPE = "filterType"; // "category" or "area"

    private RecyclerView rvMealsGrid;
    private ShimmerFrameLayout shimmerLayout;
    private View emptyStateLayout;
    private View layoutError;
    private TextView tvMealsCount;
    private MealListPresenter presenter;
    private String title;
    private String image;
    private FilterType filterType;

    public MealListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get args and parse FilterType
        if (getArguments() != null) {
            String typeValue = getArguments().getString(ARG_FILTER_TYPE, FilterType.CATEGORY.getValue());
            filterType = FilterType.fromString(typeValue);
            title = getArguments().getString(ARG_TITLE, "");
            image = getArguments().getString(ARG_IMAGE, "");
        }


        // Validate top bar title
        if (title == null || title.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Invalid " + filterType.getValue(), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }

        // Bind views
        rvMealsGrid = view.findViewById(R.id.rvMealsGrid);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        layoutError = view.findViewById(R.id.layoutError);
        Button btnRetry = layoutError.findViewById(R.id.btnRetry);

        TextView tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);
        // Set title
        tvCategoryTitle.setText(title);
        ImageView ivCategoryHeaderImage = view.findViewById(R.id.ivCategoryHeaderImage);

        // Load image with Glide
        if (image != null && !image.isEmpty()) {
            Glide.with(getContext())
                    .load(image)
                    .placeholder(R.drawable.aklati_logo)
                    .error(R.drawable.aklati_logo)
                    .centerCrop()
                    .into(ivCategoryHeaderImage);
        } else {
            ivCategoryHeaderImage.setImageResource(R.drawable.aklati_logo);
        }

        tvMealsCount = view.findViewById(R.id.tvMealsCount);
        MaterialButton btnBack = view.findViewById(R.id.btnBack);

        // Setup RecyclerView
        rvMealsGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvMealsGrid.setNestedScrollingEnabled(false);
        rvMealsGrid.setHasFixedSize(true);

        // Back button
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        // Retry button
        btnRetry.setOnClickListener(v -> retryLoadData());

        // Initialize presenter and load data
        MealRepository repo = new MealRepository(RetrofitClient.getService());
        presenter = new MealListPresenter(this, repo);

        // Delay data loading slightly to ensure UI is ready
        view.postDelayed(() -> {
            if (presenter != null) {
                loadData();
            }
        }, 300);
    }

    private void loadData() {
        if (presenter == null) return;
        switch (filterType) {
            case AREA:
                presenter.getMealsByArea(title);
                break;
            case CATEGORY:
            default:
                presenter.getMealsByCategory(title);
                break;
        }
    }

    @Override
    public void showLoading() {
        if (shimmerLayout != null) {
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmer();
        }
        if (rvMealsGrid != null) rvMealsGrid.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.GONE);
        if (layoutError != null) layoutError.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        if (shimmerLayout != null) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        if (rvMealsGrid == null || meals == null) return;

        rvMealsGrid.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);

        String countText = meals.size() + " recipe" + (meals.size() == 1 ? "" : "s") + " found";
        if (tvMealsCount != null) {
            tvMealsCount.setText(countText);
        }
        MealListAdapter adapter = new MealListAdapter(meals, meal -> {
            if (meal == null) return;
            Bundle args = new Bundle();
            args.putString(MealDetailsFragment.ARG_MEAL_ID, meal.getId());
            try {
                Navigation.findNavController(requireView()).navigate(R.id.action_categoryMeals_to_mealDetails, args);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Cannot open meal details", Toast.LENGTH_SHORT).show();
            }
        });
        rvMealsGrid.setAdapter(adapter);
    }

    @Override
    public void showEmptyState() {
        if (rvMealsGrid != null) rvMealsGrid.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.VISIBLE);
        if (layoutError != null) layoutError.setVisibility(View.GONE);
        if (tvMealsCount != null) {
            tvMealsCount.setText(getString(R.string.no_recipes_found));
        }
    }

    @Override
    public void showErrorMessage(String error) {
        // Hide other views
        if (rvMealsGrid != null) rvMealsGrid.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.GONE);

        // Show error layout
        if (layoutError != null) {
            layoutError.setVisibility(View.VISIBLE);
        }
    }

    private void retryLoadData() {
        if (presenter != null) {
            loadData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.detachView();
    }
}


