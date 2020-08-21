package components.common.builder

import react.*
import react.dom.div
import react.dom.label

class Control : RComponent<ControlProps, RState>() {
    override fun RBuilder.render() {
        div("control") {
            label("label") {
                props.labelFor?.let { attrs["htmlFor"] = it }
                +props.text
            }
            div("control-element") {
                child(buildElement(props.children))
            }
        }
    }
}

interface ControlProps : RProps {
    var children: RBuilder.() -> Unit
    var labelFor: String?
    var text: String
}

fun RBuilder.control(text: String, labelFor: String? = "", handler: RBuilder.() -> Unit) = child(Control::class) {
    attrs.text = text
    attrs.labelFor = labelFor
    attrs.children = handler
}
