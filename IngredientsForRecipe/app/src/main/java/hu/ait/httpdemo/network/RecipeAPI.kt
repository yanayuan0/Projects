package hu.ait.httpdemo.network

import hu.ait.httpdemo.data.instruction.InstructionResultItem
import hu.ait.httpdemo.data.recipe.RecipeResultItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeAPI {
    @GET("recipes/findByIngredients")
    suspend fun getRecipe(
        @Query("ingredients") ingredients: String,
        @Query("number") limit: Int = 3,
        @Query("ranking") ranking: Int = 2,
        @Query("apiKey") apiKey: String,
    ): List<RecipeResultItem>

    @GET("recipes/{id}/analyzedInstructions")
    suspend fun getAnalyzedInstructions(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): List<InstructionResultItem>
}
