package com.example.aklati.presentation.favorite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aklati.R;
import com.example.aklati.data.local.db.AppDatabase;
import com.example.aklati.data.local.entity.FavoriteMeal;
import com.example.aklati.data.local.prefs.SharedPrefsHelper;
import com.example.aklati.data.repository.FavoriteRepository;
import com.example.aklati.presentation.meal_details.MealDetailsFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteContract.View {

    private FavoriteContract.Presenter presenter;
    private RecyclerView rvFavorites;
    private LinearLayout layoutEmptyState;
    private ShimmerFrameLayout shimmerLayout;
    private FavoriteMealAdapter adapter;

    public FavoriteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);

        // Get current user ID
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(requireContext());
        String userId = prefsHelper.getCurrentUserId();

        // Log for debugging
        Log.d("FavoriteFragment", "User ID from SharedPrefs: " + userId);

        // Check if user is logged in
        if (userId == null || userId.isEmpty()) {
            Log.e("FavoriteFragment", "User not logged in - userId is null");
            showEmptyState();
            Snackbar.make(view, "Please login to view favorites", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Create repository with user ID
        AppDatabase database = AppDatabase.getInstance(requireContext());
        FavoriteRepository repository = new FavoriteRepository(database.favoriteMealDao(), userId);

        presenter = new FavoritePresenter(this, repository);

        setupRecyclerView();
        setupSwipeToDelete();

        presenter.getFavorites();
    }

    private void setupRecyclerView() {
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.setHasFixedSize(true);
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int direction) {
                int position = vh.getBindingAdapterPosition();
                if (adapter != null) {
                    FavoriteMeal removedMeal = adapter.getMealAt(position);
                    adapter.removeItem(position);
                    presenter.removeFavorite(removedMeal.getMealId());

                    Snackbar.make(rvFavorites, getString(R.string.removed_from_favorites), Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), v -> {
                        adapter.restoreItem(removedMeal, position);
                        rvFavorites.scrollToPosition(position);
                    }).show();
                }
            }
        };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rvFavorites);
    }

    @Override
    public void showLoading() {
        if (shimmerLayout != null) {
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmer();
        }
        if (rvFavorites != null) {
            rvFavorites.setVisibility(View.GONE);
        }
        if (layoutEmptyState != null) {
            layoutEmptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoading() {
        if (shimmerLayout != null) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFavorites(List<FavoriteMeal> favoriteMeals) {
        hideLoading();
        layoutEmptyState.setVisibility(View.GONE);
        rvFavorites.setVisibility(View.VISIBLE);
        adapter = new FavoriteMealAdapter(favoriteMeals, new FavoriteMealAdapter.OnFavoriteActionListener() {
            @Override
            public void onRemoveFavorite(FavoriteMeal meal, int position) {
                FavoriteMeal removedMeal = adapter.getMealAt(position);
                adapter.removeItem(position);
                presenter.removeFavorite(meal.getMealId());
                Snackbar.make(rvFavorites, getString(R.string.removed_from_favorites), Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), v -> {
                    adapter.restoreItem(removedMeal, position);
                    rvFavorites.scrollToPosition(position);
                }).show();
            }

            @Override
            public void onMealClick(FavoriteMeal meal) {
                Bundle args = new Bundle();
                args.putString(MealDetailsFragment.ARG_MEAL_ID, meal.getMealId());
                NavHostFragment.findNavController(FavoriteFragment.this).navigate(R.id.action_favorite_to_mealDetails, args);
            }
        });
        rvFavorites.setAdapter(adapter);
    }

    @Override
    public void showEmptyState() {
        hideLoading();
        rvFavorites.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(String error) {
        Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
