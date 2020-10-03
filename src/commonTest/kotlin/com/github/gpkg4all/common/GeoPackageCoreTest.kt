package com.github.gpkg4all.common

import kotlin.test.Test
import kotlin.test.assertEquals

class GeoPackageCoreTest {

    companion object {
        val application_id_gp10 = listOf("PRAGMA application_id=0x47503130;")
        val application_id_gp11 = listOf("PRAGMA application_id=0x47503131;")
        val application_id_gpkg = listOf("PRAGMA application_id=0x47504B47;")
        private val undefined_cartesian_srs_10 = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'Undefined cartesian SRS',
                -1,
                'NONE',
                -1,
                'Undefined',
                'undefined cartesian coordinate reference system'
            );""".trimIndent()
        private val undefined_cartesian_srs_11 = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'Undefined cartesian SRS',
                -1,
                'NONE',
                -1,
                'undefined',
                'undefined cartesian coordinate reference system'
            );""".trimIndent()
        private val undefined_geographic_srs_10 = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'Undefined geographic SRS',
                0,
                'NONE',
                0,
                'Undefined',
                'undefined geographic coordinate reference system'
            );""".trimIndent()
        private val undefined_geographic_srs_11 = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'Undefined geographic SRS',
                0,
                'NONE',
                0,
                'undefined',
                'undefined geographic coordinate reference system'
            );""".trimIndent()
        private val wgs_84_10 = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'WGS 84 geodetic',
                4326,
                'EPSG',
                4326,
                'GEOGCS["WGS 84",DATUM["World Geodetic System 1984",SPHEROID["WGS 84",6378137,298.257223563,AUTHORITY["EPSG","7030"]],AUTHORITY["EPSG","6326"]],PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]],UNIT["degree",0.017453292519943278,AUTHORITY["EPSG","9102"]],AUTHORITY["EPSG","4326"]',
                'longitude/latitude coordinates in decimal degrees on the WGS 84 spheroid'
            );""".trimIndent()
        private val wgs_84_12 = """
            INSERT INTO gpkg_spatial_ref_sys values(
                'WGS 84 geodetic',
                4326,
                'EPSG',
                4326,
                'GEOGCS["WGS 84",DATUM["WGS_1984",SPHEROID["WGS 84",6378137,298.257223563,AUTHORITY["EPSG","7030"]],AUTHORITY["EPSG","6326"]],PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]],UNIT["degree",0.01745329251994328,AUTHORITY["EPSG","9122"]],AUTHORITY["EPSG","4326"]]',
                'longitude/latitude coordinates in decimal degrees on the WGS 84 spheroid'
            );""".trimIndent()
        val gpkg_spatial_ref_sys_10 = listOf(
            wgs_84_10,
            undefined_cartesian_srs_10,
            undefined_geographic_srs_10
        )
        val gpkg_spatial_ref_sys_11 = listOf(
            wgs_84_10,
            undefined_cartesian_srs_11,
            undefined_geographic_srs_11
        )
        val gpkg_spatial_ref_sys_12_13 = listOf(
            wgs_84_12,
            undefined_cartesian_srs_11,
            undefined_geographic_srs_11
        )
        val gpkg_spatial_ref_sys = """
            CREATE TABLE gpkg_spatial_ref_sys (
                srs_name TEXT NOT NULL,
                srs_id INTEGER NOT NULL PRIMARY KEY,
                organization TEXT NOT NULL,
                organization_coordsys_id INTEGER NOT NULL,
                definition  TEXT NOT NULL,
                description TEXT
            );""".trimIndent()
        val gpkg_contents = """
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
            );""".trimIndent()
    }


    /**
     * Validates the core schema creation for version [1.3.0](https://www.geopackage.org/spec/#_core).
     *
     * Checks the following requirements:
     *
     * - [Requirement 2](https://www.geopackage.org/spec/#r2). Values of pragmas `application_id` and `user_version`.
     * - [Requirement 10](https://www.geopackage.org/spec/#r10). Creates table `gpkg_spatial_ref_sys`.
     * - [Requirement 11](https://www.geopackage.org/spec/#r11). Populates table `gpkg_spatial_ref_sys`.
     * - [Requirement 13](https://www.geopackage.org/spec/#r13). Creates table `gpkg_contents`.
     */
    @Test
    fun createBareCore130() {
        val expected = application_id_gpkg +
                "PRAGMA user_version=10300;" +
                gpkg_spatial_ref_sys +
                gpkg_spatial_ref_sys_12_13 +
                gpkg_contents
        val result = builder(Specs.spec130)
        assertEquals(expected.size, result.size)
        expected.zip(result).mapIndexed { idx, pair ->
            assertEquals(pair.first, pair.second, "$idx term differs")
        }
    }

    /**
     * Validates the core schema creation for version [1.2.1](https://www.geopackage.org/spec121/#_core).
     *
     * Checks the following requirements:
     *
     * - [Requirement 2](https://www.geopackage.org/spec121/#r2). Values of pragmas `application_id` and `user_version`.
     * - [Requirement 10](https://www.geopackage.org/spec121/#r10). Creates table `gpkg_spatial_ref_sys`.
     * - [Requirement 11](https://www.geopackage.org/spec121/#r11). Populates table `gpkg_spatial_ref_sys`.
     * - [Requirement 13](https://www.geopackage.org/spec121/#r13). Creates table `gpkg_contents`.
     */
    @Test
    fun createBareCore121() {
        val expected = application_id_gpkg +
                "PRAGMA user_version=10201;" +
                gpkg_spatial_ref_sys +
                gpkg_spatial_ref_sys_12_13 +
                gpkg_contents
        val result = builder(Specs.spec121)
        assertEquals(expected.size, result.size)
        expected.zip(result).mapIndexed { idx, pair ->
            assertEquals(pair.first, pair.second, "$idx term differs")
        }
    }


    /**
     * Validates the core schema creation for version [1.2.0](https://www.geopackage.org/spec120/#_core).
     *
     * Checks the following requirements:
     *
     * - [Requirement 2](https://www.geopackage.org/spec120/#r2). Values of pragmas `application_id` and `user_version`.
     * - [Requirement 10](https://www.geopackage.org/spec120/#r10). Creates table `gpkg_spatial_ref_sys`.
     * - [Requirement 11](https://www.geopackage.org/spec120/#r11). Populates table `gpkg_spatial_ref_sys`.
     * - [Requirement 13](https://www.geopackage.org/spec120/#r13). Creates table `gpkg_contents`.
     */
    @Test
    fun createBareCore120() {
        val expected = application_id_gpkg +
                "PRAGMA user_version=10200;" +
                gpkg_spatial_ref_sys +
                gpkg_spatial_ref_sys_12_13 +
                gpkg_contents
        val result = builder(Specs.spec120)
        assertEquals(expected.size, result.size)
        expected.zip(result).mapIndexed { idx, pair ->
            assertEquals(pair.first, pair.second, "$idx term differs")
        }
    }


    /**
     * Validates the core schema creation for version [1.1.0](https://www.geopackage.org/spec110/index.html#_core).
     *
     * Checks the following requirements:
     *
     * - Values of pragma `application_id`.
     * - Creates table `gpkg_spatial_ref_sys`.
     * - Populates table `gpkg_spatial_ref_sys`.
     * - Creates table `gpkg_contents`.
     */
    @Test
    fun createBareCore110() {
        val expected = application_id_gp11 +
                gpkg_spatial_ref_sys +
                gpkg_spatial_ref_sys_11 +
                gpkg_contents
        val result = builder(Specs.spec110)
        assertEquals(expected.size, result.size)
        expected.zip(result).mapIndexed { idx, pair ->
            assertEquals(pair.first, pair.second, "$idx term differs")
        }
    }


    /**
     * Validates the core schema creation for version [1.0.1](https://www.geopackage.org/spec101/index.html#_core).
     *
     * Checks the following requirements:
     *
     * - Values of pragma `application_id`.
     * - Creates table `gpkg_spatial_ref_sys`.
     * - Populates table `gpkg_spatial_ref_sys`.
     * - Creates table `gpkg_contents`.
     */
    @Test
    fun createBareCore101() {
        val expected = application_id_gp10 +
                gpkg_spatial_ref_sys +
                gpkg_spatial_ref_sys_10 +
                gpkg_contents
        val result = builder(Specs.spec101)
        assertEquals(expected.size, result.size)
        expected.zip(result).mapIndexed { idx, pair ->
            assertEquals(pair.first, pair.second, "$idx term differs")
        }
    }
}