package components.common.extension

import components.utils.addDefaults
import react.*
import react.dom.div
import react.dom.span

interface ExtensionProps : RProps {
    var refButton: RMutableRef<Nothing?>
}

private val extensionComponent = functionalComponent<ExtensionProps> {
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
    child(addDefaults(extensionComponent, "Extension")) {
        attrs {
            handler()
        }
    }
