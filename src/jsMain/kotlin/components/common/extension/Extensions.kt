package components.common.extension

import components.common.builder.Button
import components.utils.functionalComponent
import components.utils.preventDefault
import components.utils.useWindowsUtils
import org.w3c.dom.HTMLElement
import react.RBuilder
import react.RMutableRef
import react.RProps
import react.child
import react.dom.div
import react.dom.span
import react.redux.useDispatch
import reducer.ShowExtensionsDialog
import redux.WrapperAction

external interface ExtensionsProps : RProps {
    var refButton: RMutableRef<HTMLElement?>
}

val extensionsComponent = functionalComponent<ExtensionsProps>("Extensions") { props ->

    val windowsUtils = useWindowsUtils()
    val dispatch = useDispatch<ShowExtensionsDialog, WrapperAction>()

    div("control") {
        div("dependency-header") {
            span("label") {
                +"Extensions"
            }
            Button({
                id = "explore-dependencies"
                hotkey = windowsUtils.symb + " + B"
                onClick = preventDefault { dispatch(ShowExtensionsDialog) }
                refButton = props.refButton
            }) {
                +"Add "
                span(classes = "desktop-only") {
                    +"extensions"
                }
                +"..."
            }
        }
        ExtensionsList()
    }
}

@Suppress("FunctionName")
fun RBuilder.Extensions(
    ref: RMutableRef<HTMLElement?>
): Boolean {
    child(extensionsComponent) {
        attrs.refButton = ref
    }
    return true
}
