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

external interface ControlProps : RProps {
    var labelFor: String?
    var text: String
}

private val controlComponent = { children: RBuilder.() -> Unit ->
    functionalComponent(
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
                child(buildElement(children))
            }
        }
    }
}

fun RBuilder.control(label: String, htmlLabelFor: String? = null, handler: RBuilder.() -> Unit) =
    child(controlComponent(handler)) {
        attrs {
            text = label
            htmlLabelFor?.let { labelFor = it }
        }
    }
