package components.common.builder

import components.utils.attrsHtmlFor
import components.utils.functionalComponent
import kotlinext.js.js
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

private val control = functionalComponent(
    displayName = "Control",
    defaultProps = js {
        labelFor = ""
    }.unsafeCast<ControlProps>()
) { props ->
    div("control") {
        label("label") {
            attrsHtmlFor(props.labelFor)
            +props.text
        }
        div("control-element") {
            child(buildElement(props.children))
        }
    }
}

fun RBuilder.control(label: String, htmlLabelFor: String? = null, handler: RBuilder.() -> Unit) =
    child(control) {
        attrs {
            text = label
            htmlLabelFor?.let { labelFor = it }
            children = handler
        }
    }
