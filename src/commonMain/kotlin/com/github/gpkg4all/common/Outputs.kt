package com.github.gpkg4all.common

data class OutputTarget(
    val key: String,
    val description: String,
    val default: Boolean = false
)

object OutputTargets {
    val gpkg = OutputTarget("gpkg", "SQLite database file (gpkg)", default = true)
    val zip = OutputTarget("zip", "SQL files (zip)")
}