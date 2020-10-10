package builders

import com.github.gpkg4all.common.File
import com.github.gpkg4all.common.Spec
import com.github.gpkg4all.common.builder
import modules.sqljs.SqlJsStatic
import modules.sqljs.SqliteDriver
import org.khronos.webgl.Uint8Array

fun generateGeoPackage(sqlJs: SqlJsStatic, spec: Spec, exporter: (Uint8Array) -> Unit) {

    with(SqliteDriver(sqlJs)) {
        val fileSystem = builder(spec)
        fileSystem.children
            .filterIsInstance<File<*, List<String>>>()
            .filter { it.filename == "metadata.sql" }
            .flatMap { it.content }
            .forEach { stmt -> exec(stmt) }
        exporter(export())
        close()
    }
}
