package com.github.gpkg4all.common

data class ContentTarget(
    val key: String,
    val description: String,
    val default: Boolean = false
)

object ContentTargets {
    val metadata = ContentTarget("metadata", "GeoPackage minimal tables", default = true)
    val schema = ContentTarget("schema", "GeoPackage tables + User Tables (example)")
    val data = ContentTarget("data", "GeoPackage tables + User Tables (example) + Data (example)")
}