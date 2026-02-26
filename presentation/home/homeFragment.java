// ...existing code...

import java.util.ArrayList;
import java.util.Arrays;

public class homeFragment extends Fragment implements HomeContract.View {
    // ...existing code...
    private View errorLayout;
    private TextView tvErrorMessage;
    private Button btnRetry;
    private TextView tvRandomMealError;

    // ...existing code...

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ...existing code...
        errorLayout = view.findViewById(R.id.errorLayout);
        tvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);
        tvRandomMealError = view.findViewById(R.id.tvRandomMealError);

        btnRetry.setOnClickListener(v -> {
            errorLayout.setVisibility(View.GONE);
            presenter.getRandomMeal();
            presenter.getCategories();
        });

        // ...existing code...
    }

    @Override
    public void showRandomMealError(String error) {
        // نعرض error فوق الـ card بس - مش بنوقف الـ UI كله
        tvRandomMealError.setVisibility(View.VISIBLE);
        tvRandomMealName.setText("تعذر التحميل");
    }

    @Override
    public void showCategoriesError(String error) {
        // نعرض dummy data بدل ما نفضل loading أو نعرض فراغ
        List<Category> dummyCategories = getDummyCategories();
        showCategories(dummyCategories);
        Toast.makeText(requireContext(), "لا يوجد اتصال - عرض بيانات مؤقتة", Toast.LENGTH_SHORT).show();
    }

    private List<Category> getDummyCategories() {
        return Arrays.asList(
            new Category("Beef", R.drawable.ic_beef),
            new Category("Chicken", R.drawable.ic_chicken),
            new Category("Seafood", R.drawable.ic_seafood),
            new Category("Vegetarian", R.drawable.ic_vegetarian),
            new Category("Pasta", R.drawable.ic_pasta)
            // أضف باقي الـ categories حسب الـ drawables الموجودة عندك
        );
    }

    @Override
    public void showRandomMeal(Meal meal) {
        tvRandomMealError.setVisibility(View.GONE); // إخفاء الـ error لو اتحلّ
        // ...existing code...
    }

    // ...existing code...
}

