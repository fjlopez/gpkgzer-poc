package builders

import com.github.gpkg4all.common.File
import com.github.gpkg4all.common.Project
import com.github.gpkg4all.common.Spec
import com.github.gpkg4all.common.builder
import components.utils.saveAs
import kotlinext.js.jsObject
import modules.jszip.jszip
import modules.sqljs.SqlJsStatic
import modules.sqljs.SqliteDriver
import org.khronos.webgl.Uint8Array

/**
 * Builds a GeoPackage from the [spec] and uses an [exporter] to emit the result.
 */
fun buildGeoPackage(sqlJs: SqlJsStatic, spec: Spec, exporter: (Uint8Array) -> Unit) {

    with(SqliteDriver(sqlJs)) {
        val fileSystem = builder(spec)
        fileSystem.children
            .filterIsInstance<File<List<String>>>()
            .filter { it.filename == "metadata.sql" }
            .flatMap { it.content }
            .forEach { stmt -> exec(stmt) }
        exporter(export())
        close()
    }
}

/**
 * Creates and downloads a GeoPackage from a [project].
 */
fun downloadGeoPackage(sqlJs: SqlJsStatic, project: Project) {
    project.spec?.let { spec ->
        buildGeoPackage(sqlJs, spec) { export: Uint8Array ->
            saveAs(export, project.name + ".gpkg")
        }
    }
}

/**
 * Builds a zipped file with SQL definitions derived from the [spec] and uses an [exporter] to emit the result.
 */
fun buildZip(spec: Spec, exporter: (dynamic) -> Unit) {
    val zip = jszip()
    val tree = builder(spec)
    tree.children.forEach {
        if (it is File<*>) {
            val file = it.unsafeCast<File<List<String>>>()
            zip.file(file.filename, file.asText(file.content))
        }
    }
    zip.generateAsync(jsObject {
        type = "uint8array"
    }).then(exporter)
}

/**
 * Creates and downloads a zip file with a GeoPackage SQL specification from a [project].
 */
fun downloadZip(project: Project) {
    project.spec?.let { spec ->
        buildZip(spec) {
            if (it is Uint8Array) {
                saveAs(it.unsafeCast<Uint8Array>(), project.name + ".zip")
            }
        }
    }
}