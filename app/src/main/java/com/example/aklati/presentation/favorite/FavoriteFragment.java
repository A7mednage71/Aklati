package com.example.aklati.presentation.favorite;

import android.os.Bundle;
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
import com.example.aklati.data.models.Meal;
import com.example.aklati.presentation.meal_details.MealDetailsFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteContract.View {

    private FavoriteContract.Presenter presenter;
    private RecyclerView rvFavorites;
    private LinearLayout layoutEmptyState;
    private FavoriteMealAdapter adapter;

    public FavoriteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);

        presenter = new FavoritePresenter(this);

        setupRecyclerView();
        setupSwipeToDelete();

        presenter.getFavorites();
    }

    private void setupRecyclerView() {
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.setHasFixedSize(true);
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView rv,
                                          @NonNull RecyclerView.ViewHolder vh,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int direction) {
                        int position = vh.getBindingAdapterPosition();
                        if (adapter != null) {
                            Meal removedMeal = adapter.getMealAt(position);
                            adapter.removeItem(position);
                            presenter.removeFavorite(removedMeal);

                            Snackbar.make(rvFavorites,
                                            getString(R.string.removed_from_favorites),
                                            Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.undo), v -> {
                                        adapter.restoreItem(removedMeal, position);
                                        rvFavorites.scrollToPosition(position);
                                    })
                                    .show();
                        }
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rvFavorites);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showFavorites(List<Meal> meals) {
        layoutEmptyState.setVisibility(View.GONE);
        rvFavorites.setVisibility(View.VISIBLE);
        adapter = new FavoriteMealAdapter(meals, new FavoriteMealAdapter.OnFavoriteActionListener() {
            @Override
            public void onRemoveFavorite(Meal meal, int position) {
                adapter.removeItem(position);
                presenter.removeFavorite(meal);
                Snackbar.make(rvFavorites,
                                getString(R.string.removed_from_favorites),
                                Snackbar.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onMealClick(Meal meal) {
                Bundle args = new Bundle();
                args.putSerializable(MealDetailsFragment.ARG_MEAL, meal);
                NavHostFragment.findNavController(FavoriteFragment.this)
                        .navigate(R.id.action_favorite_to_mealDetails, args);
            }
        });
        rvFavorites.setAdapter(adapter);
    }

    @Override
    public void showEmptyState() {
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
