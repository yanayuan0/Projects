// MainActivity.kt
package hu.ait.httpdemo

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.httpdemo.ui.navigation.Screen
import hu.ait.httpdemo.ui.screen.MainScreen
import hu.ait.httpdemo.ui.screen.recipe.RecipeApiScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AppNavGraph(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onRecipeAPISelected = { ingredients ->
                    navController.navigate("${Screen.RecipeAPI.route}/${Uri.encode(ingredients)}")
                }
            )
        }
        composable(
            route = "${Screen.RecipeAPI.route}/{ingredients}",
            arguments = listOf(
                navArgument("ingredients") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val ingredients = backStackEntry.arguments?.getString("ingredients") ?: ""
            RecipeApiScreen(
                navController = navController,
                ingredients = ingredients
            )
        }
    }
}
