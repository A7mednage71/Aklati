package com.example.aklati.presentation.category_meals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aklati.R;
import com.example.aklati.data.models.Meal;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CategoryMealsFragment extends Fragment implements CategoryMealsContract.View {

    public static final String ARG_CATEGORY_NAME = "categoryName";
    public static final String ARG_CATEGORY_IMAGE_RES = "categoryImageRes";

    private RecyclerView rvMealsGrid;
    private ProgressBar progressBar;
    private View emptyStateLayout;
    private TextView tvCategoryTitle;
    private TextView tvMealsCount;

    private CategoryMealsPresenter presenter;
    private String categoryName;

    public CategoryMealsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get args
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY_NAME, "");
        }

        // Bind views
        rvMealsGrid = view.findViewById(R.id.rvMealsGrid);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);
        tvMealsCount = view.findViewById(R.id.tvMealsCount);

        MaterialButton btnBack = view.findViewById(R.id.btnBack);

        // Set category title
        if (categoryName != null && !categoryName.isEmpty()) {
            tvCategoryTitle.setText(categoryName);
        }

        // Setup RecyclerView
        rvMealsGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvMealsGrid.setNestedScrollingEnabled(false);

        // Back button
        btnBack.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack());

        // Presenter
        presenter = new CategoryMealsPresenter(this);
        presenter.getMealsByCategory(categoryName);
    }

    @Override
    public void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        if (rvMealsGrid != null) rvMealsGrid.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        if (rvMealsGrid == null) return;
        rvMealsGrid.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);

        String countText = meals.size() + " recipe" + (meals.size() == 1 ? "" : "s") + " found";
        tvMealsCount.setText(countText);

        MealGridAdapter adapter = new MealGridAdapter(meals, meal -> {
            // TODO: navigate to meal details
        });
        rvMealsGrid.setAdapter(adapter);
    }

    @Override
    public void showEmptyState() {
        if (rvMealsGrid != null) rvMealsGrid.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.VISIBLE);
        tvMealsCount.setText(getString(R.string.no_recipes_found));
    }

    @Override
    public void showErrorMessage(String error) {
        showEmptyState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.detachView();
    }
}


