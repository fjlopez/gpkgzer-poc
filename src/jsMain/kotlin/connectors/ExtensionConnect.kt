package connectors

import components.common.extension.ExtensionComponentProps
import components.common.extension.extensionComponent
import components.utils.invoke
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.RMutableRef
import react.RProps
import react.redux.Options
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

private val mapDispatchToProps: ExtensionDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = { dispatch, _ ->
    onShowExtensions = { event: Event ->
        event.preventDefault()
        dispatch(ShowExtensions)
    }
}

private val options: Options<Any, ExtensionProps, RProps, ExtensionComponentProps>.() -> Unit = {}

fun RBuilder.extension(handler: RHandler<ExtensionProps> = {}) =
    rConnect(mapDispatchToProps, options)(extensionComponent, "Extension")(handler)