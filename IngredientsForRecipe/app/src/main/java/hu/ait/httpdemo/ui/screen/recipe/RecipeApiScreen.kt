package hu.ait.httpdemo.ui.screen.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.ait.httpdemo.data.recipe.RecipeResultItem
import hu.ait.httpdemo.data.instruction.InstructionResultItem
import android.net.Uri
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.httpdemo.R
import hu.ait.httpdemo.ui.screen.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeApiScreen(
    navController: NavController,
    ingredients: String,
) {
    val decodedIngredients = Uri.decode(ingredients)
    val recipeViewModel: RecipeViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()

    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(ingredients, currentIndex) {
        recipeViewModel.getRecipes(decodedIngredients, currentIndex)
    }

    val recipeUiState by recipeViewModel.recipeUiState.collectAsState()

    LaunchedEffect(recipeUiState) {
        if (recipeUiState is RecipeUiState.Success) {
            val state = recipeUiState as RecipeUiState.Success
            mainViewModel.updateRecipeName(decodedIngredients, state.recipeData.title ?: "Unnamed Recipe")
            mainViewModel.updateCurrentIndex(decodedIngredients, currentIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.recipe_details)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { currentIndex += 1 }) {
                        Icon(Icons.Filled.Refresh, contentDescription = stringResource(R.string.try_another))
                    }
                }
            )
        },
        content = { innerPadding ->
            when (val state = recipeUiState) {
                is RecipeUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is RecipeUiState.Success -> {
                    RecipeDataScreen(
                        recipe = state.recipeData,
                        instructions = state.instructions,
                        innerPadding = innerPadding
                    )
                }
                is RecipeUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_data_available))
                    }
                }
            }
        }
    )
}
