package builders

import com.github.gpkg4all.common.File
import com.github.gpkg4all.common.Project
import com.github.gpkg4all.common.Spec
import com.github.gpkg4all.common.builder
import components.utils.downloadFile
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
        buildGeoPackage(sqlJs, spec) {
            downloadFile(
                filename = "${project.name}.gpkg",
                mimetype = "octet/stream",
                content = it
            )
        }
    }
}

/**
 * Builds a zipped file with SQL definitions derived from the [spec] and uses an [exporter] to emit the result.
 */
fun buildZip(name: String, spec: Spec, exporter: (dynamic) -> Unit) {
    val zip = jszip()
    val tree = builder(spec, name = name)
    tree.children.forEach {
        if (it is File<*>) {
            zip.file(it.filename, it.toText())
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
        buildZip(project.name, spec) {
            downloadFile(
                filename = "${project.name}.zip",
                mimetype = "octet/stream",
                content = it
            )
        }
    }
}