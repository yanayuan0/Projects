package hu.ait.httpdemo.data.instruction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstructionResultItem(
    @SerialName("name")
    var name: String? = null,
    @SerialName("steps")
    var steps: List<Step?>? = null
)