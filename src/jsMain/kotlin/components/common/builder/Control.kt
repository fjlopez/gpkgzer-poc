package components.common.builder

import components.utils.functionalComponent
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.dom.div
import react.dom.label

interface ControlProps : RProps {
    var children: RBuilder.() -> Unit
    var labelFor: String?
    var text: String
}

private val control = functionalComponent<ControlProps>(
    displayName = "Control",
    defaultProps = {
        labelFor = ""
    }
) { props ->
    div("control") {
        label("label") {
            props.labelFor?.let { attrs.htmlFor = it }
            +props.text
        }
        div("control-element") {
            child(buildElement(props.children))
        }
    }
}

fun RBuilder.control(text: String, labelFor: String? = null, handler: RBuilder.() -> Unit) =
    child(control) {
        attrs.text = text
        labelFor?.let { attrs.labelFor = it }
        attrs.children = handler
    }
