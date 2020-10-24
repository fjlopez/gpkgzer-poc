package config

import com.github.gpkg4all.common.*
import components.common.Theme

object Configuration {
    val theme: Theme = Theme.LIGHT
    val supportedTargets = listOf(
        OutputTargets.gpkg,
        OutputTargets.zip,
    )
    val supportedSpecifications = listOf(
        Specs.spec130, Specs.spec121, Specs.spec120, Specs.spec110, Specs.spec101,
    )
    val supportedContents = listOf(
        ContentTargets.metadata,
        ContentTargets.schema,
        ContentTargets.data,
    )
    val supportedOptions = listOf(
        ModuleInstance(Modules.features, default = true),
        ModuleInstance(Modules.tiles),
        ModuleInstance(Modules.attributes),
        ModuleInstance(Modules.extensions, default = true),
    )
    val supportedExtensions = listOf(
        ModuleInstance(Modules.nonLinearGeometryTypes),
        ModuleInstance(Modules.userDefinedGeometryTypes),
        ModuleInstance(Modules.rTreeSpatialIndexes),
        ModuleInstance(Modules.geometryTypeTriggers),
        ModuleInstance(Modules.geometrySrsIDTRiggers),
        ModuleInstance(Modules.zoomOtherIntervals),
        ModuleInstance(Modules.tilesEncodingWebP),
        ModuleInstance(Modules.metadata, default = true),
        ModuleInstance(Modules.schema),
        ModuleInstance(Modules.wktForCRS, default = true),
        ModuleInstance(Modules.tiledGriddedCoverageData),
        ModuleInstance(Modules.relatedTables),
    )
}
