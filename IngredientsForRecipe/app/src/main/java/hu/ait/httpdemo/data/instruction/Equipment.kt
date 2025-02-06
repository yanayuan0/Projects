package hu.ait.httpdemo.data.instruction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Equipment(
    @SerialName("id")
    var id: Int? = null,
    @SerialName("image")
    var image: String? = null,
    @SerialName("localizedName")
    var localizedName: String? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("temperature")
    var temperature: Temperature? = null
)