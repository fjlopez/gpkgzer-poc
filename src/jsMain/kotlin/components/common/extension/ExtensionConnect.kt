package components.common.extension

import components.utils.invoke
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.RMutableRef
import react.RProps
import react.redux.rConnect
import reducer.ShowExtensions
import redux.RAction
import redux.WrapperAction

external interface ExtensionProps : RProps {
    var refButton: RMutableRef<HTMLElement?>
}

external interface ExtensionDispatchProps : RProps {
    var onShowExtensions: (Event) -> Unit
}

fun RBuilder.extension(handler: RHandler<ExtensionProps> = {}) =
    rConnect<RAction, WrapperAction, ExtensionDispatchProps, ExtensionComponentProps>(
        { dispatch, _ ->
            onShowExtensions = { event: Event ->
                event.preventDefault()
                dispatch(ShowExtensions)
            }
        }
    ).invoke(extensionComponent, "Extension").invoke(handler)