package components.common.builder

import components.utils.functionalComponent
import kotlinext.js.jsObject
import modules.react.hotkeys.globalHotKeys
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child

interface HotKeysProps : RProps {
    var onExtensions: (Event) -> Unit
}

private val hotkeys = functionalComponent<HotKeysProps>(
    displayName = "HotKeys"
) { props ->
    globalHotKeys {
        attrs {
            keyMap = jsObject {
                extension = arrayOf("command+b", "ctrl+b")
            }
            handlers = jsObject {
                extension = props.onExtensions
            }
        }
    }
}

fun RBuilder.hotkeys(handler: HotKeysProps.() -> Unit) =
    child(hotkeys) {
        attrs {
            handler()
        }
    }


