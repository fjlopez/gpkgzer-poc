@file:JsModule("react-copy-to-clipboard")
@file:JsNonModule

package modules.react.copytoclipboard

import react.RClass
import react.RProps

@JsName("CopyToClipboard")
external val CopyToClipboard: RClass<CopyToClipboardProps>

external interface CopyToClipboardProps : RProps {
    var onCopy: (() -> Unit)?
    var text: String
}
