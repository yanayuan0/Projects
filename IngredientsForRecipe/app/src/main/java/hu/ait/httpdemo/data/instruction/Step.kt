package hu.ait.httpdemo.data.instruction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Step(
    @SerialName("equipment")
    var equipment: List<Equipment?>? = null,
    @SerialName("ingredients")
    var ingredients: List<Ingredient?>? = null,
    @SerialName("length")
    var length: Length? = null,
    @SerialName("number")
    var number: Int? = null,
    @SerialName("step")
    var step: String? = null
)