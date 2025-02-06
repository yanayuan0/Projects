package hu.ait.httpdemo.data.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsedIngredient(
    @SerialName("aisle")
    var aisle: String? = null,
    @SerialName("amount")
    var amount: Double? = null,
    @SerialName("extendedName")
    var extendedName: String? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("image")
    var image: String? = null,
    @SerialName("meta")
    var meta: List<String?>? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("original")
    var original: String? = null,
    @SerialName("originalName")
    var originalName: String? = null,
    @SerialName("unit")
    var unit: String? = null,
    @SerialName("unitLong")
    var unitLong: String? = null,
    @SerialName("unitShort")
    var unitShort: String? = null
)