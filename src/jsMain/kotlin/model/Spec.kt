package model

data class Spec(
    val key: String,
    val description: String,
    val deprecated: Boolean = false,
    val development: Boolean = false,
    val default: Boolean = false
)