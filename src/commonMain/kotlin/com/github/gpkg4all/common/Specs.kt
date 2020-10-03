package com.github.gpkg4all.common

data class Spec(
    val key: String,
    val description: String,
    val deprecated: Boolean = false,
    val development: Boolean = false,
    val default: Boolean = false,
    val features: List<Feature> = emptyList()
) : Comparable<Spec> {
    override fun compareTo(other: Spec): Int = key.compareTo(other.key)
}

object Specs {
    val spec130 = Spec("1.3.0", "1.3.0", features = listOf(
        Feature(value = applicationId120),
        Feature(value = userVersion130),
        Feature(value = gpkgSpatialRefSys101),
        Feature(value = wgs84geodetic120),
        Feature(value = undefinedCartesianSRS110),
        Feature(value = undefinedGeographicSRS110),
        Feature(value = gpkgContents101)
    ))
    val spec121 = Spec("1.2.1", "1.2.1", default = true, features = listOf(
        Feature(value = applicationId120),
        Feature(value = userVersion121),
        Feature(value = gpkgSpatialRefSys101),
        Feature(value = wgs84geodetic120),
        Feature(value = undefinedCartesianSRS110),
        Feature(value = undefinedGeographicSRS110),
        Feature(value = gpkgContents101)
    ))
    val spec120 = Spec("1.2.0", "1.2.0", features = listOf(
        Feature(value = applicationId120),
        Feature(value = userVersion120),
        Feature(value = gpkgSpatialRefSys101),
        Feature(value = wgs84geodetic120),
        Feature(value = undefinedCartesianSRS110),
        Feature(value = undefinedGeographicSRS110),
        Feature(value = gpkgContents101)
    ))
    val spec110 = Spec("1.1.0", "1.1.0", features = listOf(
        Feature(value = applicationId110),
        Feature(value = gpkgSpatialRefSys101),
        Feature(value = wgs84geodetic101),
        Feature(value = undefinedCartesianSRS110),
        Feature(value = undefinedGeographicSRS110),
        Feature(value = gpkgContents101)
    ))
    val spec101 = Spec("1.0.1", "1.0.1", features = listOf(
        Feature(value = applicationId101),
        Feature(value = gpkgSpatialRefSys101),
        Feature(value = wgs84geodetic101),
        Feature(value = undefinedCartesianSRS101),
        Feature(value = undefinedGeographicSRS101),
        Feature(value = gpkgContents101)
    ), deprecated = true)
}