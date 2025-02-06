package hu.ait.httpdemo.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object RecipeAPI : Screen("recipeApiScreen")
}