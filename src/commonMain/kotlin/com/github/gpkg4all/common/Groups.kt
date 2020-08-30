package com.github.gpkg4all.common

data class Group(
    val key: String,
    val title: String
) : Comparable<Group> {
    override fun compareTo(other: Group): Int = key.compareTo(other.key)
}

object Groups {
    val options = Group("0-options", "Options")
    val general = Group("1-general", "General")
    val srs = Group("2-srs", "Spatial Reference Systems")
    val features = Group("3-features", "Features")
    val tiles = Group("4-tiles", "Tiles")
    val coverages = Group("5-coverages", "Coverages")
    val relatedTables = Group("6-relatedTables", "Related Tables")
}