package components.common.form

import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent


private fun <T: Any> radioComponent() = functionalComponent<RadioProps<T>> { props ->

    div("group-radio") {
        props.options.forEach { option ->
            child(tickInput<T>(TickType.RADIO)) {
                key = option.toString()
                attrs.checked = props.error == null && option == props.selected
                attrs.text = props.asText.invoke(option)
                attrs.value = option
                attrs.disabled = props.disabled
                attrs.handler = props.onChange
            }
        }
        props.error?.let { error ->
            child(tickInput<T>(TickType.RADIO)) {
                key = (props.options.size + 1).toString()
                attrs.checked = true
                attrs.text = error
                attrs.disabled = props.disabled
                attrs.error = true
            }
        }
    }
}.also {
    val component = it.asDynamic()
    component.displayName = "RadioGroup"
    component.defaultProps = js("{}")
    component.defaultProps.name = ""
    component.defaultProps.options = emptyList<T>()
    component.defaultProps.onChange = {}
    component.defaultProps.disabled = false
    component.defaultProps.asText = { "replace me" }
}

fun <T: Any> RBuilder.radioGroup(handler: RadioProps<T>.() -> Unit) = child(radioComponent<T>()) {
    attrs {
        handler()
    }
}

interface RadioProps<T: Any> : RProps {
    var name: String
    var selected: T?
    var error: String?
    var onChange: (T) -> Unit
    var options: List<T>
    var disabled: Boolean
    var asText: (T) -> String
}

