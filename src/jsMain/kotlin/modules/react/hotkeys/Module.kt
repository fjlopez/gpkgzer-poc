/**
 * See https://fkhadra.github.io/react-toastify/introduction
 */

@file:JsModule("react-hotkeys")
@file:JsNonModule

package modules.react.hotkeys

import org.w3c.dom.HTMLElement
import react.RClass
import react.RMutableRef
import react.RProps

@JsName("GlobalHotKeys")
external val globalHotKeys: RClass<GlobalHotKeysProps>

external interface GlobalHotKeysProps : RProps {
    var keyMap: dynamic
    var handlers: dynamic
}

@JsName("HotKeys")
external val hotKeys: RClass<HotKeysProps>

external interface HotKeysProps : RProps {
    var keyMap: dynamic
    var handlers: dynamic
    var innerRef: RMutableRef<HTMLElement?>
}
