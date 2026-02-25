package com.example.aklati.presentation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aklati.R;
import com.example.aklati.data.models.Category;
import com.example.aklati.data.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class homeFragment extends Fragment implements HomeContract.View {

    private RecyclerView rvCategories;
    private EditText etSearch;

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
        rvCategories = view.findViewById(R.id.rvCategories);
        etSearch = view.findViewById(R.id.etSearch);
        rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setNestedScrollingEnabled(false);

        etSearch.setOnClickListener(v -> navigateToSearch());

        showCategories(getDummyCategories());
    }

    private List<Category> getDummyCategories() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("Beef", R.drawable.aklati_logo));
        list.add(new Category("Chicken", R.drawable.aklati_logo));
        list.add(new Category("Dessert", R.drawable.aklati_logo));
        list.add(new Category("Lamb", R.drawable.aklati_logo));
        list.add(new Category("Pasta", R.drawable.aklati_logo));
        list.add(new Category("Seafood", R.drawable.aklati_logo));
        list.add(new Category("Vegan", R.drawable.aklati_logo));
        list.add(new Category("Vegetarian", R.drawable.aklati_logo));
        return list;
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showRandomMeal(Meal meal) {
    }

    @Override
    public void showCategories(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(categories, category -> {
            Bundle args = new Bundle();
            args.putString("categoryName", category.getName());
            args.putInt("categoryImageRes", category.getDrawableResId());
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_home_to_categoryMeals, args);
        });
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void showErrorMessage(String error) {
    }

    @Override
    public void navigateToDetails(Meal meal) {
    }

    @Override
    public void navigateToSearch() {
        Navigation.findNavController(requireView()).navigate(R.id.searchFragment);
    }
}