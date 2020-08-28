package components.common.builder

import components.utils.disableTab
import components.utils.functionalComponent
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

interface ButtonProps : RProps {
    var hotkey: String?
    var block: RDOMBuilder<SPAN>.() -> Unit
    var primary: Boolean
    var disabled: Boolean
    var id: String
    var refButton: RRef?
    var onClick: (Event) -> Unit
}

private val button = functionalComponent<ButtonProps>(
    displayName = "Button",
    defaultProps = {
        hotkey = ""
        block = {}
        primary = false
        disabled = false
        onClick = { console.info("Clicked!") }
    }) { props ->
    button(
        classes = "button ${if (props.primary) "primary" else ""}",
        type = ButtonType.button
    ) {
        attrs.id = props.id
        attrs.disabled = props.disabled
        attrs.onClickFunction = props.onClick
        props.refButton?.let { ref = it }
        span("button-content") {
            attrs.disableTab()
            span(block = props.block)
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

fun RBuilder.button(handler: ButtonProps.() -> Unit, block: RDOMBuilder<SPAN>.() -> Unit) =
    child(button) {
        attrs {
            handler()
            this.block = block
        }
    }
