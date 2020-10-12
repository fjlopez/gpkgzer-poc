package components.common.builder

import components.utils.attrsHtmlFor
import components.utils.functionalComponent
import kotlinext.js.jsObject
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import react.RBuilder
import react.RProps
import react.*
import react.dom.*

external interface FieldInputProps : RProps {
    var id: String
    var value: String
    var text: String
    var disabled: Boolean?
    var onChange: (String) -> Unit
}

private val fieldInputComponent =
    functionalComponent(
        displayName = "FieldInput",
        defaultProps = jsObject<FieldInputProps> {
            disabled = false
        }
    ) { props ->
        var currentValue by useState(props.value)
        div("control control-inline") {
            label {
                attrsHtmlFor(props.id)
                + props.text
            }
            input(InputType.text, classes = "input") {
                attrs {
                    id = props.id
                    value = currentValue
                    onChangeFunction = { event ->
                        currentValue = event.target.asDynamic().value as String
                    }
                    onBlurFunction = { _ ->
                        props.onChange(currentValue)
                    }
                    props.disabled?.let { disabled = it }
                }
            }
        }
    }

fun RBuilder.fieldInput(handler: FieldInputProps.() -> Unit) =
    child(fieldInputComponent) {
        attrs {
            handler()
        }
    }