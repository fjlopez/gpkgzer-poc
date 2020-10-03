package builders

import com.github.gpkg4all.common.Spec
import com.github.gpkg4all.common.builder
import modules.sqljs.SqlJsStatic
import modules.sqljs.SqliteDriver
import org.khronos.webgl.Uint8Array

fun generateGeoPackage(sqlJs: SqlJsStatic, spec: Spec, exporter: (Uint8Array) -> Unit) {

    with(SqliteDriver(sqlJs)) {
        val statements = builder(spec)
        statements.forEach { stmt -> exec(stmt) }
        exporter(export())
        close()
    }
}
