package components.common.builder

import kotlinext.js.jsObject
import react.*
import react.dom.div
import react.dom.label

private val control = functionalComponent<ControlProps> { props ->
    div("control") {
        label("label") {
            props.labelFor?.let { attrs["htmlFor"] = it }
            +props.text
        }
        div("control-element") {
            child(buildElement(props.children))
        }
    }
}.also {
    val component = it.asDynamic()
    component.displayName = "Control"
    component.defaultProps = jsObject<ControlProps> {
        labelFor = ""
    }
}

interface ControlProps : RProps {
    var children: RBuilder.() -> Unit
    var labelFor: String?
    var text: String
}

fun RBuilder.control(text: String, labelFor: String? = null, handler: RBuilder.() -> Unit) =
    child(control) {
        attrs.text = text
        labelFor?.let { attrs.labelFor = it }
        attrs.children = handler
    }
