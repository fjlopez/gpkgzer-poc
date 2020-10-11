package components.common.builder

import components.utils.disableTab
import components.utils.functionalComponent
import kotlinext.js.jsObject
import kotlinx.html.ButtonType
import kotlinx.html.SPAN
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.RRef
import react.child
import react.dom.RDOMBuilder
import react.dom.button
import react.dom.span

external interface ButtonProps : RProps {
    var hotkey: String?
    var primary: Boolean
    var disabled: Boolean
    var id: String?
    var refButton: RRef?
    var onClick: (Event) -> Unit
}

private val buttonComponent = { block: RDOMBuilder<SPAN>.() -> Unit ->
    functionalComponent(
        displayName = "Button",
        defaultProps = jsObject<ButtonProps> {
            hotkey = ""
            primary = false
            disabled = false
            onClick = { console.info("Clicked!") }
        }
    ) { props ->
        button(
            classes = "button ${if (props.primary) "primary" else ""}",
            type = ButtonType.button
        ) {
            attrs {
                props.id?.let { id = it }
                disabled = props.disabled
                onClickFunction = props.onClick
                props.refButton?.let { ref = it }
            }
            span("button-content") {
                attrs.disableTab()
                span(block = block)
                props.hotkey?.let {
                    if (it.isNotEmpty()) {
                        span("secondary desktop-only") {
                            +it
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.button(handler: ButtonProps.() -> Unit, block: RDOMBuilder<SPAN>.() -> Unit) =
    child(buttonComponent(block)) {
        attrs {
            handler()
        }
    }
