package hu.ait.httpdemo.data.instruction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Length(
    @SerialName("number")
    var number: Int? = null,
    @SerialName("unit")
    var unit: String? = null
)