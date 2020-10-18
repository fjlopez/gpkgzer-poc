package connectors

import com.github.gpkg4all.common.ModuleInstance
import components.common.extension.ExtensionDialogComponentProps
import components.common.extension.extensionDialogComponent
import components.utils.connects
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AddExtension
import reducer.AppState
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

private val mapStateToProps: ExtensionDialogStateProps.(AppState, RProps) -> Unit = { state, _ ->
    isShown = state.showExtensionsDialog
    extensions = state.availableExtensions
}

private val mapDispatchToProps: ExtensionDialogDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit =
    { dispatch, _ ->
        onAddExtension = { dispatch(AddExtension(it)) }
    }

private val options: Options<AppState, ExtensionDialogProps, ExtensionDialogStateProps, ExtensionDialogComponentProps>.() -> Unit =
    {
    }

fun RBuilder.extensionDialog(onClose: (Event) -> Unit) =
    rConnect(mapStateToProps, mapDispatchToProps, options).connects(
        displayName = "ExtensionDialog",
        component = extensionDialogComponent
    ).invoke {
        attrs.onClose = onClose
    }