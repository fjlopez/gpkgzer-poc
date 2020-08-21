package model

data class OutputTarget(
    val key: String,
    val description: String,
    val default: Boolean = false
)