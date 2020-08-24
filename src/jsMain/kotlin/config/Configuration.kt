package config

import components.common.Theme
import model.*

object Configuration {
    val theme: Theme = Theme.LIGHT
    val supportedTargets = listOf(
        OutputTarget("gpkg", "SQLite database file (gpkg)", default = true),
        OutputTarget("sql", "SQL files (sql)"),
    )
    val supportedSpecifications = listOf(
        Specs.spec131, Specs.spec130, Specs.spec121, Specs.spec120, Specs.spec110, Specs.spec101, Specs.spec100
    )
    val supportedContents = listOf(
        ContentTargets.metadata,
        ContentTargets.schema,
        ContentTargets.data,
    )
    val options = listOf(
        ModuleInstance(Modules.features),
        ModuleInstance(Modules.tiles),
        ModuleInstance(Modules.attributes),
        ModuleInstance(Modules.extensions),
    )
    val supportedExtensions = listOf(
        Modules.nonLinearGeometryTypes,
        Modules.userDefinedGeometryTypes,
        Modules.rTreeSpatialIndexes,
        Modules.geometryTypeTriggers,
        Modules.geometrySrsIDTRiggers,
        Modules.zoomOtherIntervals,
        Modules.tilesEncodingWebP,
        Modules.metadata,
        Modules.schema,
        Modules.wktForCRS,
        Modules.tiledGriddedCoverageData,
        Modules.relatedTables,
    )
}
