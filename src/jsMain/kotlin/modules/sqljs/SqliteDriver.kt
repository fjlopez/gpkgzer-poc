package modules.sqljs

import org.khronos.webgl.Uint8Array

class SqliteDriver(init: SqlJsStatic) {
    private val db: Database = init.Database.invoke()
    fun exec(sql: String): Array<QueryResults> = db.exec(sql)
    fun export(): Uint8Array = db.export()
    fun close(): Unit = db.close()
}