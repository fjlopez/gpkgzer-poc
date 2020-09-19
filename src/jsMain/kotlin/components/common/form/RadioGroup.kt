package components.common.form

import components.utils.functionalComponent
import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.child
import react.dom.div

external interface RadioProps<T : Any> : RProps {
    var name: String
    var selected: T?
    var error: String?
    var onChange: (T) -> Unit
    var options: List<T>
    var disabled: Boolean
    var asText: (T) -> String
}

private fun <T : Any> radioComponent() = functionalComponent(
    displayName = "RadioGroup",
    defaultProps = jsObject<RadioProps<T>> {
        name = ""
        options = emptyList()
        onChange = {}
        disabled = false
        asText = { "replace me" }
    }
) { props ->
    div("group-radio") {
        props.options.forEach {
            child(tickInput<T>(TickType.RADIO)) {
                key = it.toString()
                attrs {
                    checked = props.error == null && it == props.selected
                    text = props.asText.invoke(it)
                    value = it
                    disabled = props.disabled
                    handler = props.onChange
                }
            }
        }
        props.error?.let {
            child(tickInput<T>(TickType.RADIO)) {
                key = (props.options.size + 1).toString()
                attrs {
                    checked = true
                    text = it
                    disabled = props.disabled
                    error = true
                }
            }
        }
    }
}

fun <T : Any> RBuilder.radioGroup(handler: RadioProps<T>.() -> Unit) =
    child(radioComponent<T>()) {
        attrs.handler()
    }


