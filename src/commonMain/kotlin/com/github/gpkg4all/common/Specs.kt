package com.github.gpkg4all.common

data class Spec(
    val key: String,
    val description: String,
    val deprecated: Boolean = false,
    val development: Boolean = false,
    val default: Boolean = false
) : Comparable<Spec> {
    override fun compareTo(other: Spec): Int = key.compareTo(other.key)
}

object Specs {
    val spec131 = Spec("1.3.1", "1.3.1 (SNAPSHOT)", development = true)
    val spec130 = Spec("1.3.0", "1.3.0 IS", default = true)
    val spec121 = Spec("1.2.1", "1.2.1 ISc")
    val spec120 = Spec("1.2.0", "1.2.0 IS")
    val spec110 = Spec("1.1.0", "1.1.0 IS")
    val spec101 = Spec("1.0.1", "1.0.1 D-ISc", deprecated = true)
    val spec100 = Spec("1.0.0", "1.0.0 D-IS", deprecated = true)
}