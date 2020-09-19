package components.common.extension

import com.github.gpkg4all.common.ModuleInstance
import components.utils.invoke
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.RProps
import react.redux.rConnect
import reducer.AddExtension
import reducer.State
import redux.RAction
import redux.WrapperAction

external interface ExtensionDialogProps : RProps {
    var onClose: (Event) -> Unit
}

external interface ExtensionDialogStateProps : RProps {
    var isShown: Boolean
    var extensions: List<ModuleInstance>
}

external interface ExtensionDialogDispatchProps : RProps {
    var onAddExtension: (ModuleInstance) -> Unit
}

fun RBuilder.extensionDialog(handler: RHandler<ExtensionDialogProps> = {}) =
    rConnect<State, RAction, WrapperAction, ExtensionDialogProps, ExtensionDialogStateProps, ExtensionDialogDispatchProps, ExtensionDialogComponentProps>(
        { state, _ ->
            isShown = state.showExtensionsDialog
            extensions = state.availableExtensions
        },
        { dispatch, _ ->
            onAddExtension = { dispatch(AddExtension(it)) }
        }
    ).invoke(extensionDialogComponent, "ExtensionDialog").invoke(handler)