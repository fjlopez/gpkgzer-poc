package components.common.form

import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent


private fun <T: Any> checkBoxGroupComponent() = functionalComponent<CheckBoxGroupProps<T>> { props ->
    div("group-radio") {
        props.options.forEach { option ->
            child(tickInput<T>(TickType.CHECKBOX)) {
                key = option.toString()
                attrs.checked = props.error == null && option in props.selected
                attrs.text = props.asText.invoke(option)
                attrs.value = option
                attrs.disabled = props.disabled
                attrs.handler = props.onChange
            }
        }
        props.error?.let { error ->
            child(tickInput<T>(TickType.CHECKBOX)) {
                key = (props.options.size + 1).toString()
                attrs.checked = true
                attrs.text = error
                attrs.disabled = props.disabled
                attrs.handler = {}
                attrs.error = true
            }
        }
    }
}.also {
    val component = it.asDynamic()
    component.displayName = "CheckBoxGroup"
    component.defaultProps = js("{}")
    component.defaultProps.name = ""
    component.defaultProps.selected = emptyList<T>()
    component.defaultProps.options = emptyList<T>()
    component.defaultProps.onChange = {}
    component.defaultProps.disabled = false
    component.defaultProps.asText = { "replace me" }
}

fun <T: Any> RBuilder.checkBoxGroup(handler: CheckBoxGroupProps<T>.() -> Unit) = child(checkBoxGroupComponent<T>()) {
    attrs {
        handler()
    }
}

interface CheckBoxGroupProps<T: Any> : RProps {
    var name: String
    var selected: List<T>
    var error: String?
    var onChange: (T) -> Unit
    var options: List<T>
    var disabled: Boolean
    var asText: (T) -> String
}
