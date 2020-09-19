package components.common.extension

import components.common.builder.button
import components.utils.functionalComponent
import components.utils.useWindowsUtils
import react.dom.div
import react.dom.span

interface ExtensionComponentProps : ExtensionDispatchProps, ExtensionProps

val extensionComponent = functionalComponent<ExtensionComponentProps>("Extension") { props ->

    val windowsUtils = useWindowsUtils()

    div("control") {
        div("dependency-header") {
            span("label") {
                +"Extensions"
            }
            button({
                id = "explore-dependencies"
                hotkey = windowsUtils.symb + " + B"
                onClick = props.onShowExtensions
                refButton = props.refButton
            }) {
                +"Add "
                span(classes = "desktop-only") {
                    +"extensions"
                }
                +"..."
            }
        }
        extensionList()
    }
}

