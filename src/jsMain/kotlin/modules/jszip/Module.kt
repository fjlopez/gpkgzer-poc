package modules.jszip

import kotlin.js.Promise

external interface GenerateAsyncOptions {
    var type: String
}

external class JSZip {
    fun file(name: String, data: dynamic)
    fun generateAsync(options: GenerateAsyncOptions): Promise<dynamic>
}

@JsModule("jszip")
@JsNonModule
external fun jszip(): JSZip
