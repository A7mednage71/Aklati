// ...existing code...

@Override
public void getRandomMeal() {
    // في الـ onFailure بتاع الـ random meal
    repo.getRandomMeal(new MealCallback() {
        @Override
        public void onSuccess(Meal meal) {
            view.hideLoading();
            view.showRandomMeal(meal);
        }

        @Override
        public void onFailure(String error) {
            // لا تعمل hideLoading هنا علشان الـ categories لسه شغاله
            view.showRandomMealError(error);
        }
    });
}

@Override
public void getCategories() {
    repo.getCategories(new CategoriesCallback() {
        @Override
        public void onSuccess(List<Category> categories) {
            view.hideLoading();
            view.showCategories(categories);
        }

        @Override
        public void onFailure(String error) {
            view.hideLoading();
            view.showCategoriesError(error);
        }
    });
}

