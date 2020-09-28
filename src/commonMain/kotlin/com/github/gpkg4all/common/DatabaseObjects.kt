package com.github.gpkg4all.common

import com.github.gpkg4all.common.ObjectType.*
import com.github.gpkg4all.dag.Node
import com.github.gpkg4all.dag.NodeUpdate

enum class ObjectType {
    TABLE, VIEW, TRIGGER, PRAGMA, INSERT
}

class DatabaseObject(
    val type: ObjectType,
    val name: String,
    val definition: String,
    val informative: Boolean = false,
    val requires: List<String> = emptyList()
) {
    val normative: Boolean get() = !informative
}

sealed class AbstractFeature : Node<DatabaseObject>

class Feature(
    override val value: DatabaseObject,
    override val before: List<Node<DatabaseObject>> = emptyList(),
    override val after: List<Node<DatabaseObject>> = emptyList()
) : AbstractFeature()

class UpdateFeature(
    override val value: DatabaseObject,
    override val before: List<Node<DatabaseObject>> = emptyList(),
    override val after: List<Node<DatabaseObject>> = emptyList(),
    override val deprecates: List<Node<DatabaseObject>>
) : AbstractFeature(), NodeUpdate<DatabaseObject>


val applicationId = DatabaseObject(
    type = PRAGMA,
    name = "application_id",
    definition = "PRAGMA application_id=0x47504B47;"
)

val userVersion120 = DatabaseObject(
    type = PRAGMA,
    name = "user_version",
    definition = "PRAGMA user_version=10200;"
)

val userVersion121 = DatabaseObject(
    type = PRAGMA,
    name = "user_version",
    definition = "PRAGMA user_version=10201;"
)

val wgs84geodetic = DatabaseObject(
    type = INSERT,
    name = "gpkg_spatial_ref_sys#4326",
    definition = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'WGS 84 geodetic',
                4326,
                'EPSG',
                4326,
                'GEOGCS["WGS 84",DATUM["WGS_1984",SPHEROID["WGS 84",6378137,298.257223563,AUTHORITY["EPSG","7030"]],AUTHORITY["EPSG","6326"]],PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]],UNIT["degree",0.01745329251994328,AUTHORITY["EPSG","9122"]],AUTHORITY["EPSG","4326"]]',
                'longitude/latitude coordinates in decimal degrees on the WGS 84 spheroid'
            );""".trimIndent()
)

val undefinedCartesianSRS = DatabaseObject(
    type = INSERT,
    name = "gpkg_spatial_ref_sys#-1",
    definition = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'Undefined cartesian SRS',
                -1,
                'NONE',
                -1,
                'undefined',
                'undefined cartesian coordinate reference system'
            );""".trimIndent()
)

val undefinedGeographicSRS = DatabaseObject(
    type = INSERT,
    name = "gpkg_spatial_ref_sys#0",
    definition = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'Undefined geographic SRS',
                0,
                'NONE',
                0,
                'undefined',
                'undefined geographic coordinate reference system'
            );""".trimIndent()
)

/**
 * `pkg_spatial_ref_sys` Table Definition SQL for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_spatial_ref_sys).
 */
val gpkgSpatialRefSys121 = DatabaseObject(
    type = TABLE,
    name = "gpkg_spatial_ref_sys",
    definition = """
        CREATE TABLE gpkg_spatial_ref_sys (
            srs_name TEXT NOT NULL,
            srs_id INTEGER NOT NULL PRIMARY KEY,
            organization TEXT NOT NULL,
            organization_coordsys_id INTEGER NOT NULL,
            definition  TEXT NOT NULL,
            description TEXT
        );""".trimIndent()
)

/**
 * SQL/MM View of `gpkg_spatial_ref_sys` Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_spatial_ref_sys).
 */
val stSpatialRefSys121 = DatabaseObject(
    type = VIEW,
    name = "st_spatial_ref_sys",
    definition = """
        CREATE VIEW st_spatial_ref_sys AS
          SELECT
            srs_name,
            srs_id,
            organization,
            organization_coordsys_id,
            definition,
            description
          FROM gpkg_spatial_ref_sys;
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_spatial_ref_sys")
)


/**
 * SQL/SF View of `gpkg_spatial_ref_sys` Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_spatial_ref_sys).
 */
val spatialRefSys121 = DatabaseObject(
    type = VIEW,
    name = "spatial_ref_sys",
    definition = """
        CREATE VIEW spatial_ref_sys AS
          SELECT
            srs_id AS srid,
            organization AS auth_name,
            organization_coordsys_id AS auth_srid,
            definition AS srtext
          FROM gpkg_spatial_ref_sys;
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_spatial_ref_sys")
)

/**
 * `gpkg_contents` Table Definition SQL for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_contents).
 */
val gpkgContents121 = DatabaseObject(
    type = TABLE,
    name = "gpkg_contents",
    definition = """
        CREATE TABLE gpkg_contents (
            table_name TEXT NOT NULL PRIMARY KEY,
            data_type TEXT NOT NULL,
            identifier TEXT UNIQUE,
            description TEXT DEFAULT '',
            last_change DATETIME NOT NULL DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),
            min_x DOUBLE,
            min_y DOUBLE,
            max_x DOUBLE,
            max_y DOUBLE,
            srs_id INTEGER,
            CONSTRAINT fk_gc_r_srs_id FOREIGN KEY (srs_id) REFERENCES gpkg_spatial_ref_sys(srs_id)
        );""".trimIndent(),
    requires = listOf("gpkg_spatial_ref_sys")
)

/**
 * `gpkg_geometry_columns` Table Definition SQL for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_geometry_columns).
 */
val gpkgGeometryColumns121 = DatabaseObject(
    type = TABLE,
    name = "gpkg_geometry_columns",
    definition = """
        CREATE TABLE gpkg_geometry_columns (
          table_name TEXT NOT NULL,
          column_name TEXT NOT NULL,
          geometry_type_name TEXT NOT NULL,
          srs_id INTEGER NOT NULL,
          z TINYINT NOT NULL,
          m TINYINT NOT NULL,
          CONSTRAINT pk_geom_cols PRIMARY KEY (table_name, column_name),
          CONSTRAINT uk_gc_table_name UNIQUE (table_name),
          CONSTRAINT fk_gc_tn FOREIGN KEY (table_name) REFERENCES gpkg_contents(table_name),
          CONSTRAINT fk_gc_srs FOREIGN KEY (srs_id) REFERENCES gpkg_spatial_ref_sys (srs_id)
        );
    """.trimIndent(),
    requires = listOf("gpkg_contents", "gpkg_spatial_ref_sys")
)

/**
 * SQL/MM View of `gpkg_geometry_columns` Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_geometry_columns).
 */
val stGeometryColumns121 = DatabaseObject(
    type = VIEW,
    name = "st_geometry_columns",
    definition = """
        CREATE VIEW st_geometry_columns AS
          SELECT
            table_name,
            column_name,
            "ST_" || geometry_type_name,
            g.srs_id,
            srs_name
          FROM gpkg_geometry_columns as g JOIN gpkg_spatial_ref_sys AS s
          WHERE g.srs_id = s.srs_id;
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_geometry_columns", "gpkg_spatial_ref_sys")
)

/**
 * SQL/SF View of `gpkg_geometry_columns` Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_geometry_columns).
 */
val geometryColumns121 = DatabaseObject(
    type = VIEW,
    name = "geometry_columns",
    definition = """
        CREATE VIEW geometry_columns AS
          SELECT
            table_name AS f_table_name,
            column_name AS f_geometry_column,
            code4name (geometry_type_name) AS geometry_type,
            2 + (CASE z WHEN 1 THEN 1 WHEN 2 THEN 1 ELSE 0 END) + (CASE m WHEN 1 THEN 1 WHEN 2 THEN 1 ELSE 0 END) AS coord_dimension,
            srs_id AS srid
          FROM gpkg_geometry_columns;
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_geometry_columns")
)

/**
 * `gpkg_tile_matrix_set` Table Definition SQL for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_set).
 */
val gpkgTileMatrixSet121 = DatabaseObject(
    type = TABLE,
    name = "gpkg_tile_matrix_set",
    definition = """
        CREATE TABLE gpkg_tile_matrix_set (
          table_name TEXT NOT NULL PRIMARY KEY,
          srs_id INTEGER NOT NULL,
          min_x DOUBLE NOT NULL,
          min_y DOUBLE NOT NULL,
          max_x DOUBLE NOT NULL,
          max_y DOUBLE NOT NULL,
          CONSTRAINT fk_gtms_table_name FOREIGN KEY (table_name) REFERENCES gpkg_contents(table_name),
          CONSTRAINT fk_gtms_srs FOREIGN KEY (srs_id) REFERENCES gpkg_spatial_ref_sys (srs_id)
        );
    """.trimIndent(),
    requires = listOf("gpkg_contents", "gpkg_spatial_ref_sys")
)

/**
 * `gpkg_tile_matrix` Table Definition SQL for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix).
 */
val gpkgTileMatrix121 = DatabaseObject(
    type = TABLE,
    name = "gpkg_tile_matrix",
    definition = """
        CREATE TABLE gpkg_tile_matrix (
          table_name TEXT NOT NULL,
          zoom_level INTEGER NOT NULL,
          matrix_width INTEGER NOT NULL,
          matrix_height INTEGER NOT NULL,
          tile_width INTEGER NOT NULL,
          tile_height INTEGER NOT NULL,
          pixel_x_size DOUBLE NOT NULL,
          pixel_y_size DOUBLE NOT NULL,
          CONSTRAINT pk_ttm PRIMARY KEY (table_name, zoom_level),
          CONSTRAINT fk_tmm_table_name FOREIGN KEY (table_name) REFERENCES gpkg_contents(table_name)
        );
    """.trimIndent(),
    requires = listOf("gpkg_contents")
)

/**
 * `gpkg_extensions` Table Definition SQL for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_extensions).
 */
val gpkgExtensions121 = DatabaseObject(
    type = TABLE,
    name = "gpkg_extensions",
    definition = """
        CREATE TABLE gpkg_extensions (
          table_name TEXT,
          column_name TEXT,
          extension_name TEXT NOT NULL,
          definition TEXT NOT NULL,
          scope TEXT NOT NULL,
          CONSTRAINT ge_tce UNIQUE (table_name, column_name, extension_name)
        );
    """.trimIndent()
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixZoomLevelInsert = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_zoom_level_insert",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_zoom_level_insert'
        BEFORE INSERT ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'insert on table ''gpkg_tile_matrix'' violates constraint: zoom_level cannot be less than 0')
        WHERE (NEW.zoom_level < 0);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixZoomLevelUpdate = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_zoom_level_update",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_zoom_level_update'
        BEFORE UPDATE of zoom_level ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'update on table ''gpkg_tile_matrix'' violates constraint: zoom_level cannot be less than 0')
        WHERE (NEW.zoom_level < 0);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixMatrixWidthInsert = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_matrix_width_insert",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_matrix_width_insert'
        BEFORE INSERT ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'insert on table ''gpkg_tile_matrix'' violates constraint: matrix_width cannot be less than 1')
        WHERE (NEW.matrix_width < 1);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixMatrixWidthUpdate = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_matrix_width_update",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_matrix_width_update'
        BEFORE UPDATE OF matrix_width ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'update on table ''gpkg_tile_matrix'' violates constraint: matrix_width cannot be less than 1')
        WHERE (NEW.matrix_width < 1);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixMatrixHeightInsert = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_matrix_height_insert",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_matrix_height_insert'
        BEFORE INSERT ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'insert on table ''gpkg_tile_matrix'' violates constraint: matrix_height cannot be less than 1')
        WHERE (NEW.matrix_height < 1);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixMatrixHeightUpdate = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_matrix_height_update",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_matrix_height_update'
        BEFORE UPDATE OF matrix_height ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'update on table ''gpkg_tile_matrix'' violates constraint: matrix_height cannot be less than 1')
        WHERE (NEW.matrix_height < 1);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixPixelXSizeInsert = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_pixel_x_size_insert",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_pixel_x_size_insert'
        BEFORE INSERT ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'insert on table ''gpkg_tile_matrix'' violates constraint: pixel_x_size must be greater than 0')
        WHERE NOT (NEW.pixel_x_size > 0);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixPixelXSizeUpdate = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_pixel_x_size_update",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_pixel_x_size_update'
        BEFORE UPDATE OF pixel_x_size ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'update on table ''gpkg_tile_matrix'' violates constraint: pixel_x_size must be greater than 0')
        WHERE NOT (NEW.pixel_x_size > 0);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixPixelYSizeInsert = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_pixel_y_size_insert",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_pixel_y_size_insert'
        BEFORE INSERT ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'insert on table ''gpkg_tile_matrix'' violates constraint: pixel_y_size must be greater than 0')
        WHERE NOT (NEW.pixel_y_size > 0);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)

/**
 * `gpkg_tile_matrix` Trigger Definition SQL (Informative) for version
 * [1.2.1](https://www.geopackage.org/spec121/#_gpkg_tile_matrix_2).
 */
val gpkgTileMatrixPixelYSizeUpdate = DatabaseObject(
    type = TRIGGER,
    name = "gpkg_tile_matrix_pixel_y_size_update",
    definition = """
        CREATE TRIGGER 'gpkg_tile_matrix_pixel_y_size_update'
        BEFORE UPDATE OF pixel_y_size ON 'gpkg_tile_matrix'
        FOR EACH ROW BEGIN
        SELECT RAISE(ABORT, 'update on table ''gpkg_tile_matrix'' violates constraint: pixel_y_size must be greater than 0')
        WHERE NOT (NEW.pixel_y_size > 0);
        END
    """.trimIndent(),
    informative = true,
    requires = listOf("gpkg_tile_matrix")
)
