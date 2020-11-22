package components.common.extension

import components.common.builder.Button
import components.utils.functionalComponent
import components.utils.preventDefault
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.dom.span
import react.redux.useDispatch
import reducer.ShowExtensionsDialog
import redux.WrapperAction

external interface ExtensionsProps : RProps

val extensionsComponent = functionalComponent<ExtensionsProps>("Extensions") { _ ->

    val dispatch = useDispatch<ShowExtensionsDialog, WrapperAction>()

    div("control") {
        div("dependency-header") {
            span("label") {
                +"Extensions"
            }
            Button({
                id = "explore-dependencies"
                onClick = preventDefault { dispatch(ShowExtensionsDialog) }
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
): Boolean {
    child(extensionsComponent) {
    }
    return true
}
