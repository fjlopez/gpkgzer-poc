package model

sealed class Rule

object CoreRule : Rule()

data class AtLeastOneRule(var modules: List<Module>) : Rule()

@Suppress("FunctionName")
fun AtLeastOneRule(vararg modules: Module) = AtLeastOneRule(modules.asList())


data class Module(
    val key: String,
    val title: String,
    val description: String? = null,
    val dependsOn: Rule = CoreRule,
    val extension: Boolean = false,
    val officialSince: Spec,
    val deprecatedBy: Spec? = null,
    val group: String,
    val requireUdt: Boolean = false,
)

object Modules {

    val features = Module(
        key = "features",
        title = "Features",
        officialSince = Specs.spec100,
        group = "Options",
    )

    val tiles = Module(
        key = "tiles",
        title = "Tiles",
        officialSince = Specs.spec100,
        group = "Options",
    )

    val attributes = Module(
        key = "attributes",
        title = "Attributes",
        officialSince = Specs.spec100,
        group = "Options",
    )

    val extensions = Module(
        key = "extensions",
        title = "Extensions",
        officialSince = Specs.spec100,
        group = "Options",
    )

    val nonLinearGeometryTypes = Module(
        key = "gpkg-geom",
        title = "Non-Linear Geometry Types",
        dependsOn = AtLeastOneRule(features),
        officialSince = Specs.spec100,
        description = """
            Support for the CircularString, CompoundCurve, CurvePolygon, MultiCurve, 
            MultiSurface, Curve, and Surface geometry types in user feature tables.
            """.trimIndent(),
        group = "Features",
        extension = true,
        requireUdt = true,
    )

    val userDefinedGeometryTypes = Module(
        key = "author-geom",
        title = "User Defined Geometry Types",
        dependsOn = AtLeastOneRule(features),
        officialSince = Specs.spec100,
        deprecatedBy = Specs.spec120,
        description = "Support for user defined geometry types in user feature tables.",
        group = "Features",
        extension = true,
        requireUdt = true,
    )

    val rTreeSpatialIndexes = Module(
        key = "gpkg-rtree-index",
        title = "RTree Spatial Indexes",
        dependsOn = AtLeastOneRule(features),
        officialSince = Specs.spec100,
        description = "Provides a means to encode an RTree index for geometry values in user feature tables.",
        group = "Features",
        extension = true,
        requireUdt = true,
    )

    val geometryTypeTriggers = Module(
        key = "gpkg-geometry-type-trigger",
        title = "Geometry Type Triggers",
        dependsOn = AtLeastOneRule(features),
        officialSince = Specs.spec100,
        deprecatedBy = Specs.spec120,
        description = """
            Prevents the storage in a geometry column of geometries of types
            not assignable to the geometry type specified for the column
            in user feature tables.
            """.trimIndent(),
        group = "Features",
        extension = true,
        requireUdt = true,
    )

    val geometrySrsIDTRiggers = Module(
        key = "gpkg-srs-id-trigger",
        title = "Geometry SRS ID Triggers",
        dependsOn = AtLeastOneRule(features),
        officialSince = Specs.spec100,
        deprecatedBy = Specs.spec120,
        description = """
            Prevents the storage in a geometry column of geometries with SRS
            different to the SRS specified for the column in user feature tables.
            """.trimIndent(),
        group = "Features",
        extension = true,
        requireUdt = true,
    )

    val zoomOtherIntervals = Module(
        key = "gpkg-zoom-other",
        title = "Zoom Other Intervals",
        dependsOn = AtLeastOneRule(tiles),
        officialSince = Specs.spec110,
        description = "Allows zoom level intervals other than a factor of two in user tile tables.",
        group = "Tiles",
        extension = true,
        requireUdt = true,
    )

    val tilesEncodingWebP = Module(
        key = "gpkg-webp",
        title = "Tiles Encoding WebP",
        dependsOn = AtLeastOneRule(tiles),
        officialSince = Specs.spec110,
        description = "Allows encoding of tile images in WebP format in user tile tables.",
        group = "Tiles",
        extension = true,
        requireUdt = true,
    )

    val metadata = Module(
        key = "gpkg-metadata",
        title = "Metadata",
        officialSince = Specs.spec100,
        description = "Provides a means of storing metadata.",
        group = "General",
        extension = true,
    )

    val schema = Module(
        key = "gpkg-schema",
        title = "Schema",
        dependsOn = AtLeastOneRule(features, attributes),
        officialSince = Specs.spec100,
        description = "Provides a means to describe the columns of tables.",
        group = "General",
        extension = true,
    )

    val wktForCRS = Module(
        key = "gpkg-crs-wkt",
        title = "WKT for Coordinate Reference Systems",
        dependsOn = CoreRule,
        officialSince = Specs.spec110,
        description = """
            Provides support for SRS encoded in WKT conform to" OGC Well known text representation of Coordinate
            Reference Systems".""".trimIndent(),
        group = "Spatial Reference Systems",
        extension = true,
    )

    val tiledGriddedCoverageData = Module(
        key = "gpkg-2d-gridded",
        title = "Tiled Gridded Coverage Data",
        dependsOn = AtLeastOneRule(tiles),
        officialSince = Specs.spec120,
        description = """
            Provides support for or encoding and storing 16-bit and 32-bit tiled regular grid coverages composed of
            regular gridded data".""".trimIndent(),
        group = "Coverages",
        extension = true,
    )

    val relatedTables = Module(
        key = "gpkg2d-gridded",
        title = "Related tables",
        dependsOn = AtLeastOneRule(features, attributes, tiles),
        officialSince = Specs.spec130,
        description = "Allows to establish many-to-many relationships.",
        group = "Related Tables",
        extension = true,
    )
}