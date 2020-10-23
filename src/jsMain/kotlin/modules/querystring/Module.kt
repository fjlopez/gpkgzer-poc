@file:JsModule("query-string")
@file:JsNonModule
package modules.querystring

external fun stringify(obj: dynamic): String

external fun parse(query: String): dynamic