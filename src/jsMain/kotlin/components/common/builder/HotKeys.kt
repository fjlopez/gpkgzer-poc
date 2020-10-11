package components.common.builder

import components.utils.functionalComponent
import kotlinext.js.jsObject
import modules.react.hotkeys.globalHotKeys
import react.RBuilder
import react.RProps
import react.child

external interface HotKeysProps : RProps {
    var onExtensions: () -> Unit
    var onGenerate: () -> Unit
    var onExplore: () -> Unit
}

private val hotkeys = functionalComponent<HotKeysProps>(
    displayName = "HotKeys"
) { props ->
    globalHotKeys {
        attrs {
            keyMap = jsObject {
                extension = arrayOf("command+b", "ctrl+b")
                generate = arrayOf("command+i", "ctrl+i")
                explore = arrayOf("ctrl+space")
            }
            handlers = jsObject {
                extension = props.onExtensions
                generate = props.onGenerate
                explore = props.onExplore
            }
        }
    }
}

fun RBuilder.hotkeys(handler: HotKeysProps.() -> Unit) =
    child(hotkeys) {
        attrs.handler()
    }


