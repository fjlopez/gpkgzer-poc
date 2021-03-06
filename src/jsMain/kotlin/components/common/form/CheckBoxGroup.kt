package components.common.form

import components.utils.functionalComponent
import kotlinext.js.js
import react.RBuilder
import react.RProps
import react.child
import react.dom.div

external interface CheckBoxGroupProps<T : Any> : RProps {
    var name: String
    var selected: List<T>
    var error: String?
    var onChange: (T) -> Unit
    var options: List<T>
    var disabled: Boolean
    var asText: (T) -> String
}

private fun <T : Any> checkBoxGroupComponent() = functionalComponent(
    displayName = "CheckBoxGroup",
    defaultProps = js {
        name = ""
        selected = emptyList<T>()
        options = emptyList<T>()
        onChange = {}
        disabled = false
        asText = { "replace me" }
    }.unsafeCast<CheckBoxGroupProps<T>>()
) { props ->
    div("group-radio") {
        props.options.forEach {
            child(tickInput<T>(TickType.CHECKBOX)) {
                key = it.toString()
                attrs {
                    checked = props.error == null && it in props.selected
                    text = props.asText.invoke(it)
                    value = it
                    disabled = props.disabled
                    handler = props.onChange
                }
            }
        }
        props.error?.let {
            child(tickInput<T>(TickType.CHECKBOX)) {
                key = (props.options.size + 1).toString()
                attrs {
                    checked = true
                    text = it
                    disabled = props.disabled
                    handler = {}
                    error = true
                }
            }
        }
    }
}

fun <T : Any> RBuilder.checkBoxGroup(handler: CheckBoxGroupProps<T>.() -> Unit) =
    child(checkBoxGroupComponent<T>()) {
        attrs.handler()
    }

