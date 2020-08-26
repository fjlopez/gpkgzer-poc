/**
 * See https://fkhadra.github.io/react-toastify/introduction
 */

@file:JsModule("react-hotkeys")
@file:JsNonModule

package modules.react.hotkeys

import react.RClass
import react.RProps

@JsName("GlobalHotKeys")
external val globalHotKeys: RClass<GlobalHotKeysProps>

external interface GlobalHotKeysProps : RProps {
    var keyMap: dynamic
    var handlers: dynamic
}
