package hu.ait.httpdemo.data.instruction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Temperature(
    @SerialName("number")
    var number: Double? = null,
    @SerialName("unit")
    var unit: String? = null
)