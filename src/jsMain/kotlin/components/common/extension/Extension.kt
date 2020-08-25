package components.common.extension

import components.utils.functionalComponent
import react.RBuilder
import react.RMutableRef
import react.RProps
import react.child
import react.dom.div
import react.dom.span

interface ExtensionProps : RProps {
    var refButton: RMutableRef<Nothing?>
}

private val extensionComponent = functionalComponent<ExtensionProps>("Extension") {
    div("control") {
        div("dependency-header") {
            span("label") {
                +"Extensions"
            }
        }
        extensionList {

        }
    }
}

fun RBuilder.extension(handler: ExtensionProps.() -> Unit) =
    child(extensionComponent) {
        handler(attrs)
    }
