package modules.sqljs

import org.khronos.webgl.Uint8Array

class SqliteDriver(private val db: Database) {
    fun exec(sql: String): Array<QueryResults> = db.exec(sql)
    fun export(): Uint8Array = db.export()
}