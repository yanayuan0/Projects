// RecipeViewModel.kt
package hu.ait.httpdemo.ui.screen.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.httpdemo.network.RecipeAPI
import hu.ait.httpdemo.data.recipe.RecipeResultItem
import hu.ait.httpdemo.data.instruction.InstructionResultItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

sealed class RecipeUiState {
    object Empty : RecipeUiState()
    object Loading : RecipeUiState()
    data class Success(
        val recipeData: RecipeResultItem,
        val instructions: List<InstructionResultItem>
    ) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}

@HiltViewModel
class RecipeViewModel @Inject constructor(
    val recipeAPI: RecipeAPI
) : ViewModel() {

    private val _recipeUiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Empty)
    val recipeUiState: StateFlow<RecipeUiState> = _recipeUiState

    private val currentIndexMap = mutableMapOf<String, Int>()

    fun getCurrentIndex(ingredients: String): Int {
        return currentIndexMap.getOrDefault(ingredients, 0)
    }

    fun incrementIndex(ingredients: String): Int {
        val currentIndex = currentIndexMap.getOrDefault(ingredients, 0)
        val newIndex = currentIndex + 1
        currentIndexMap[ingredients] = newIndex
        return newIndex
    }

    fun getRecipes(ingredients: String, index: Int) {
        _recipeUiState.value = RecipeUiState.Loading

        viewModelScope.launch {
            try {
//                val apiKey = "ad5cc42f2ca6488facafc4ced82d1156"
                val apiKey = "d3a85edc60ba42f9bbc8830835036a0e"

                val formattedIngredients = ingredients.split(",")
                    .map { it.trim() }
                    .joinToString(",")

                val recipes = recipeAPI.getRecipe(
                    ingredients = formattedIngredients,
                    apiKey = apiKey,
                    limit = 3
                )

                if (recipes.isNotEmpty()) {
                    val safeIndex = index % recipes.size
                    val recipe = recipes[safeIndex]
                    val recipeId = recipe.id ?: 0

                    val instructions = recipeAPI.getAnalyzedInstructions(recipeId, apiKey)

                    _recipeUiState.value = RecipeUiState.Success(recipe, instructions)
                } else {
                    _recipeUiState.value = RecipeUiState.Error("No recipes found")
                }
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching recipes", e)
                val errorMessage = when (e) {
                    is IOException -> "Network error: ${e.message}"
                    is HttpException -> "HTTP error ${e.code()}: ${e.message()}"
                    is SerializationException -> "Serialization error: ${e.message}"
                    else -> "Unexpected error: ${e.localizedMessage}"
                }
                _recipeUiState.value = RecipeUiState.Error(errorMessage)
            }
        }
    }
}
