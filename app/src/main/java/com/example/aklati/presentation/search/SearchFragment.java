package com.example.aklati.presentation.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.aklati.presentation.category_meals.MealGridAdapter;

import java.util.List;

public class SearchFragment extends Fragment implements SearchContract.View {

    private EditText etSearch;
    private ImageView ivClearSearch;
    private RecyclerView rvSearchResults;
    private ProgressBar progressBar;
    private View emptyStateLayout;
    private View initialStateLayout;
    private TextView tvEmptyQuery;
    private TextView tvResultsCount;

    private SearchPresenter presenter;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind views
        etSearch = view.findViewById(R.id.etSearch);
        ivClearSearch = view.findViewById(R.id.ivClearSearch);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        initialStateLayout = view.findViewById(R.id.initialStateLayout);
        tvEmptyQuery = view.findViewById(R.id.tvEmptyQuery);
        tvResultsCount = view.findViewById(R.id.tvResultsCount);

        // Setup RecyclerView
        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvSearchResults.setNestedScrollingEnabled(false);

        // Presenter
        presenter = new SearchPresenter(this);

        // Search input listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                ivClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
                presenter.searchMeals(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Clear button
        ivClearSearch.setOnClickListener(v -> etSearch.setText(""));
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        initialStateLayout.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        rvSearchResults.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        initialStateLayout.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.VISIBLE);

        String countText = meals.size() + " result" + (meals.size() == 1 ? "" : "s") + " found";
        tvResultsCount.setText(countText);

        MealGridAdapter adapter = new MealGridAdapter(meals, meal -> {
            Bundle args = new Bundle();
            args.putSerializable("meal", meal);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_search_to_mealDetails, args);
        });
        rvSearchResults.setAdapter(adapter);
    }

    @Override
    public void showEmptyState(String query) {
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
        initialStateLayout.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.GONE);

        tvEmptyQuery.setText(getString(R.string.search_no_results_for, query));
    }

    @Override
    public void showInitialState() {
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        initialStateLayout.setVisibility(View.VISIBLE);
        tvResultsCount.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(String error) {
        showEmptyState(error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.detachView();
    }
}

