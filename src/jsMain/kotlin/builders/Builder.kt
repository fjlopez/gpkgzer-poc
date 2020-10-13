package builders

import com.github.gpkg4all.common.File
import com.github.gpkg4all.common.Spec
import com.github.gpkg4all.common.builder
import kotlinext.js.jsObject
import modules.jszip.jszip
import modules.sqljs.SqlJsStatic
import modules.sqljs.SqliteDriver
import org.khronos.webgl.Uint8Array

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
