package components.common.builder

import components.utils.disableTab
import components.utils.functionalComponent
import kotlinext.js.js
import kotlinx.html.ButtonType
import kotlinx.html.SPAN
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.*
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
        defaultProps = js {
            hotkey = ""
            primary = false
            disabled = false
            onClick = { console.info("Clicked!") }
        }.unsafeCast<ButtonProps>()
    ) { props ->
        button(
            classes = "button ${if (props.primary) "primary" else ""}",
            type = ButtonType.button
        ) {
            props.id?.let { attrs.id = it }
            attrs.disabled = props.disabled
            attrs.onClickFunction = props.onClick
            props.refButton?.let {
                ref = it
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
