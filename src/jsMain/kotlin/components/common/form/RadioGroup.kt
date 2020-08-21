package components.common.form

import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent


private fun <T : Any> radioComponent() = functionalComponent<RadioProps<T>> { props ->

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
}.also {
    val component = it.asDynamic()
    component.displayName = "RadioGroup"
    component.defaultProps = jsObject<RadioProps<T>> {
        name = ""
        options = emptyList()
        onChange = {}
        disabled = false
        asText = { "replace me" }
    }
}

fun <T : Any> RBuilder.radioGroup(handler: RadioProps<T>.() -> Unit) = child(radioComponent<T>()) {
    attrs {
        handler()
    }
}

interface RadioProps<T : Any> : RProps {
    var name: String
    var selected: T?
    var error: String?
    var onChange: (T) -> Unit
    var options: List<T>
    var disabled: Boolean
    var asText: (T) -> String
}

