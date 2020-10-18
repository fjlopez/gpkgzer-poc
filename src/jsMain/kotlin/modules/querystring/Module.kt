@file:JsModule("query-string")

package modules.querystring

external fun stringify(obj: dynamic): String

external fun parse(query: String): dynamic