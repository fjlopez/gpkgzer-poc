package components.common.form

import components.utils.functionalComponent
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RProps
import react.dom.a
import react.dom.span

enum class TickType {
    RADIO, CHECKBOX
}

interface TickInputProps<T : Any> : RProps {
    var checked: Boolean
    var text: String
    var value: T
    var handler: (T) -> Unit
    var disabled: Boolean
    var error: Boolean
}

internal fun <T : Any> tickInput(type: TickType) = functionalComponent<TickInputProps<T>>(
    displayName = when (type) {
        TickType.RADIO -> "Radio"
        TickType.CHECKBOX -> "CheckBox"
    },
    defaultProps = {
        checked = false
        text = ""
        handler = {}
        disabled = false
        error = false
    }
) { props ->
    val className = when (type) {
        TickType.RADIO -> "radio"
        TickType.CHECKBOX -> "checkbox"
    }

    fun onClick(value: T) = { event: Event ->
        event.preventDefault()
        props.handler(value)
    }
    if (props.disabled || props.error) {
        span(classes = "$className ${if (props.checked) "checked" else ""} ${if (props.error) "err" else ""}") {
            span("caret") { attrs["tabIndex"] = -1 }
            span("$className-content") { +props.text }
        }
    } else {
        a("/", classes = "$className ${if (props.checked) "checked" else ""}") {
            attrs.onClickFunction = onClick(props.value)
            span("caret") { attrs["tabIndex"] = -1 }
            span("$className-content") { +props.text }
        }
    }
}
