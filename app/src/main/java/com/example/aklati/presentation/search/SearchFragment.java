package com.example.aklati.presentation.search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aklati.R;
import com.example.aklati.data.local.db.AppDatabase;
import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.models.Meal;
import com.example.aklati.data.remote.network.RetrofitClient;
import com.example.aklati.data.repository.FavoriteRepository;
import com.example.aklati.data.repository.MealRepository;
import com.example.aklati.presentation.meal_details.MealDetailsFragment;
import com.example.aklati.presentation.meal_list.MealListAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class SearchFragment extends Fragment implements SearchContract.View {

    private EditText etSearch;
    private ImageView ivClearSearch;
    private RecyclerView rvSearchResults;
    private ShimmerFrameLayout shimmerLayout;
    private View emptyStateLayout;
    private View initialStateLayout;
    private View layoutError;
    private Button btnRetry;
    private TextView tvEmptyQuery;
    private TextView tvResultsCount;

    private SearchPresenter presenter;
    private MealListAdapter currentAdapter;

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
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        initialStateLayout = view.findViewById(R.id.initialStateLayout);
        layoutError = view.findViewById(R.id.layoutError);
        btnRetry = view.findViewById(R.id.btnRetry);
        tvEmptyQuery = view.findViewById(R.id.tvEmptyQuery);
        tvResultsCount = view.findViewById(R.id.tvResultsCount);

        // Setup RecyclerView
        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvSearchResults.setNestedScrollingEnabled(false);
        rvSearchResults.setHasFixedSize(true);

        // Create Repository and Presenter
        MealRepository repository = new MealRepository(RetrofitClient.getService());

        // Initialize FavoriteRepository
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(requireContext());
        String userId = prefsHelper.getCurrentUserId();
        AppDatabase database = AppDatabase.getInstance(requireContext());
        FavoriteRepository favoriteRepository = new FavoriteRepository(database.favoriteMealDao(), userId);

        presenter = new SearchPresenter(this, repository, favoriteRepository);

        // Retry button
        btnRetry.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                presenter.searchMeals(query);
            }
        });

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

        // Handle keyboard search action
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });

        // Clear button
        ivClearSearch.setOnClickListener(v -> {
            etSearch.setText("");
            etSearch.requestFocus();
            showKeyboard();
        });
    }

    private void hideKeyboard() {
        if (getActivity() != null && etSearch != null) {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }

    private void showKeyboard() {
        if (getActivity() != null && etSearch != null) {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void showLoading() {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        initialStateLayout.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        if (rvSearchResults == null || meals == null) return;

        rvSearchResults.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        initialStateLayout.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.VISIBLE);

        String countText = meals.size() + " result" + (meals.size() == 1 ? "" : "s") + " found";
        tvResultsCount.setText(countText);

        MealListAdapter adapter = new MealListAdapter(meals,
                // 1. Navigation Click Listener
                meal -> {
                    if (meal == null) return;
                    Bundle args = new Bundle();
                    args.putString(MealDetailsFragment.ARG_MEAL_ID, meal.getId());
                    try {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_search_to_mealDetails, args);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Cannot open meal details", Toast.LENGTH_SHORT).show();
                    }
                },
                // 2. Favorite Click Listener (نفس اللوجيك المكرر)
                new MealListAdapter.OnFavoriteClickListener() {
                    @Override
                    public void onFavoriteClick(Meal meal, int position) {
                        if (presenter != null) {
                            presenter.onFavoriteClick(meal.getId());
                        }
                    }

                    @Override
                    public void checkFavoriteStatus(Meal meal, ImageView favoriteIcon) {
                        if (presenter != null) {
                            presenter.checkFavoriteStatus(meal.getId(), favoriteIcon);
                        }
                    }
                }
        );
        currentAdapter = adapter;
        rvSearchResults.setAdapter(adapter);
    }

    @Override
    public void showEmptyState(String query) {
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
        initialStateLayout.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.GONE);
        tvEmptyQuery.setText(getString(R.string.search_no_results_for, query));
    }

    @Override
    public void showInitialState() {
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        initialStateLayout.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(String error) {
        rvSearchResults.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
        initialStateLayout.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvResultsCount.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFavoriteIcon(String mealId, boolean isFavorite) {
        if (currentAdapter != null && rvSearchResults != null) {
            currentAdapter.updateFavoriteIcon(mealId, isFavorite, rvSearchResults);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.detachView();
    }
}