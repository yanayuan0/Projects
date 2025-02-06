package hu.ait.httpdemo.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.httpdemo.network.RecipeAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

data class RecipeSuggestion(
    val id: Int,
    val ingredients: String,
    var recipeName: String
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recipeAPI: RecipeAPI
) : ViewModel() {

    private var lastId = 0
    private val _recipeSuggestions = MutableStateFlow<List<RecipeSuggestion>>(emptyList())
    val recipeSuggestions: StateFlow<List<RecipeSuggestion>> = _recipeSuggestions

    private val currentIndexMap = mutableMapOf<String, Int>()

    fun addIngredients(ingredients: String) {
        viewModelScope.launch {
            try {
//                val apiKey = "ad5cc42f2ca6488facafc4ced82d1156"
                val apiKey = "d3a85edc60ba42f9bbc8830835036a0e"
                val formattedIngredients = ingredients.split(",").map { it.trim() }.joinToString(",")
                val recipes = recipeAPI.getRecipe(
                    ingredients = formattedIngredients,
                    apiKey = apiKey,
                )

                val recipeName = if (recipes.isNotEmpty()) {
                    recipes.first().title ?: "Unnamed Recipe"
                } else {
                    "No recipe found"
                }

                val newSuggestion = RecipeSuggestion(
                    id = ++lastId,
                    ingredients = ingredients,
                    recipeName = recipeName
                )

                _recipeSuggestions.value += newSuggestion
                currentIndexMap[ingredients] = 0
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching recipes", e)
                val newSuggestion = RecipeSuggestion(
                    id = ++lastId,
                    ingredients = ingredients,
                    recipeName = "Error fetching recipe"
                )
                _recipeSuggestions.value += newSuggestion
                currentIndexMap[ingredients] = 0
            }
        }
    }

    fun updateRecipeName(ingredients: String, newRecipeName: String) {
        val updatedSuggestions = _recipeSuggestions.value.map { suggestion ->
            if (suggestion.ingredients == ingredients) {
                suggestion.copy(recipeName = newRecipeName)
            } else {
                suggestion
            }
        }
        _recipeSuggestions.value = updatedSuggestions
    }

    fun deleteSuggestion(suggestion: RecipeSuggestion) {
        _recipeSuggestions.value = _recipeSuggestions.value.filter { it.id != suggestion.id }
        currentIndexMap.remove(suggestion.ingredients)
    }

    fun updateSuggestion(suggestion: RecipeSuggestion, newIngredients: String) {
        viewModelScope.launch {
            try {
//                val apiKey = "ad5cc42f2ca6488facafc4ced82d1156"
                val apiKey = "d3a85edc60ba42f9bbc8830835036a0e"

                val formattedIngredients = newIngredients.split(",").map { it.trim() }.joinToString(",")
                val recipes = recipeAPI.getRecipe(
                    ingredients = formattedIngredients,
                    apiKey = apiKey,
                )

                val recipeName = if (recipes.isNotEmpty()) {
                    recipes.first().title ?: "Unnamed Recipe"
                } else {
                    "No recipe found"
                }

                val updatedSuggestion = suggestion.copy(
                    ingredients = newIngredients,
                    recipeName = recipeName
                )

                _recipeSuggestions.value = _recipeSuggestions.value.map {
                    if (it.id == suggestion.id) updatedSuggestion else it
                }

                currentIndexMap.remove(suggestion.ingredients)
                currentIndexMap[newIngredients] = 0
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error updating recipe", e)
            }
        }
    }

    fun updateCurrentIndex(ingredients: String, index: Int) {
        currentIndexMap[ingredients] = index
    }

    fun getCurrentIndexFor(ingredients: String): Int {
        return currentIndexMap[ingredients] ?: 0
    }
}
