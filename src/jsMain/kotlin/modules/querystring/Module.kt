@file:JsModule("query-string")
@file:JsNonModule

package modules.querystring

external fun stringify(obj: dynamic, options: dynamic = definedExternally): String

external fun parse(query: String, options: dynamic = definedExternally): dynamic