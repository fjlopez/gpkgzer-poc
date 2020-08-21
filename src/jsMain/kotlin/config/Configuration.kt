package config

import components.common.Theme
import model.ContentTarget
import model.Module
import model.OutputTarget
import model.Spec

object Configuration {
    val theme: Theme = Theme.LIGHT
    val supportedTargets = listOf(
        OutputTarget("gpkg", "SQLite database file (gpkg)", default = true),
        OutputTarget("sql", "SQL files (sql)"),
    )
    val supportedSpecifications = listOf(
        Spec("1.3.1", "1.3.1 (SNAPSHOT)", development = true),
        Spec("1.3.0", "1.3.0 IS", default = true),
        Spec("1.2.1", "1.2.1 ISc"),
        Spec("1.2.0", "1.2.0 IS"),
        Spec("1.1.0", "1.1.0 IS"),
        Spec("1.0.1", "1.0.1 D-ISc", deprecated = true),
        Spec("1.0.0", "1.0.0 D-IS", deprecated = true),
    )
    val supportedContents = listOf(
        ContentTarget("metadata", "GeoPackage minimal tables", default = true),
        ContentTarget("schema", "GeoPackage tables + user defined tables (example)"),
        ContentTarget("data", "GeoPackage tables + user defined tables (example) + data (example)"),
    )
    val options = listOf(
        Module("features", "Features"),
        Module("tiles", "Tiles"),
        Module("attributes", "Attributes"),
        Module("extensions", "Extensions"),
    )
}