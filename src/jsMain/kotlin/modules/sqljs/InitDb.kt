package modules.sqljs

import kotlinext.js.jsObject
import org.khronos.webgl.Uint8Array
import kotlin.js.Promise

operator fun InitStatementJsStatic.invoke(): Statement =
    createInstance(this).unsafeCast<Statement>()

operator fun InitDatabaseJsStatic.invoke(): Database =
    createInstance(this).unsafeCast<Database>()

operator fun InitDatabaseJsStatic.invoke(data: Array<Number>): Database =
    createInstance(this, data).unsafeCast<Database>()

operator fun InitDatabaseJsStatic.invoke(data: Uint8Array): Database =
    createInstance(this, data).unsafeCast<Database>()

operator fun InitSqlJsStatic.invoke(): Promise<SqlJsStatic> = asDynamic()().unsafeCast<Promise<SqlJsStatic>>()
operator fun InitSqlJsStatic.invoke(config: Config?): Promise<SqlJsStatic> = asDynamic()(config).unsafeCast<Promise<SqlJsStatic>>()

@Suppress("UNUSED_VARIABLE", "UNUSED_PARAMETER")
fun createInstance(type: dynamic, vararg args: dynamic): dynamic {
    val argsArray = (listOf(null) + args).toTypedArray()
    return js("new (Function.prototype.bind.apply(type, argsArray))")
}

fun initDb(config: Config? = jsObject()): Promise<SqlJsStatic> = initSqlJs(config)

