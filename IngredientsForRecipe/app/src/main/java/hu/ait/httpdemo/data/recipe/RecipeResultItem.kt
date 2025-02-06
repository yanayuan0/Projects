package hu.ait.httpdemo.data.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeResultItem(
    @SerialName("id")
    var id: Int? = null,
    @SerialName("image")
    var image: String? = null,
    @SerialName("imageType")
    var imageType: String? = null,
    @SerialName("likes")
    var likes: Int? = null,
    @SerialName("missedIngredientCount")
    var missedIngredientCount: Int? = null,
    @SerialName("missedIngredients")
    var missedIngredients: List<MissedIngredient?>? = null,
    @SerialName("title")
    var title: String? = null,
    @SerialName("unusedIngredients")
    var unusedIngredients: List<UnusedIngredient?>? = null,
    @SerialName("usedIngredientCount")
    var usedIngredientCount: Int? = null,
    @SerialName("usedIngredients")
    var usedIngredients: List<UsedIngredient?>? = null
)