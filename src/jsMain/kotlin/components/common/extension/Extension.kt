package components.common.extension

import components.common.builder.button
import components.utils.functionalComponent
import components.utils.useWindowsUtils
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RMutableRef
import react.RProps
import react.child
import react.dom.div
import react.dom.span
import reducer.ShowExtensions
import reducer.store

interface ExtensionProps : RProps {
    var refButton: RMutableRef<HTMLElement?>
}

private val extensionComponent = functionalComponent<ExtensionProps>("Extension") { props ->

    val windowsUtils = useWindowsUtils()

    div("control") {
        div("dependency-header") {
            span("label") {
                +"Extensions"
            }
            button({
                id = "explore-dependencies"
                hotkey = windowsUtils.symb + " + B"
                onClick = { event: Event ->
                    event.preventDefault()
                    store.dispatch(ShowExtensions)
                }
                refButton = props.refButton
            }) {
                +"Add "
                span(classes = "desktop-only") {
                    +"extensions"
                }
                +"..."
            }
        }
        extensionList {}
    }
}

fun RBuilder.extension(handler: ExtensionProps.() -> Unit) =
    child(extensionComponent) {
        attrs.handler()
    }
