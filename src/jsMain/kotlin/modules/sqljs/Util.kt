package modules.sqljs

import kotlinext.js.jsObject
import modules.sqljs.*
import org.khronos.webgl.Uint8Array
import kotlin.js.Promise

operator fun InitStatementJsStatic.invoke(): Statement =
    createInstance(this)
operator fun InitDatabaseJsStatic.invoke(): Database =
    createInstance(this)
operator fun InitDatabaseJsStatic.invoke(data: Array<Number>): Database =
    createInstance(this, data)
operator fun InitDatabaseJsStatic.invoke(data: Uint8Array): Database =
    createInstance(this, data)
operator fun InitSqlJsStatic.invoke(): Promise<SqlJsStatic> = asDynamic()()
operator fun InitSqlJsStatic.invoke(config: Config?): Promise<SqlJsStatic> = asDynamic()(config)

@Suppress("UNUSED_VARIABLE", "UNUSED_PARAMETER")
fun createInstance(type: dynamic, vararg args: dynamic): dynamic {
    val argsArray = (listOf(null) + args).toTypedArray()
    return js("new (Function.prototype.bind.apply(type, argsArray))")
}

fun initDb(config: Config? = jsObject()): Promise<SqlJsStatic> = initSqlJs(config)

class SqliteDriver(private val db: Database) {
    fun exec(sql: String): Array<QueryResults> = db.exec(sql)
    fun export(): Uint8Array = db.export()
}