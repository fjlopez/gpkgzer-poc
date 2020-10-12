package connectors

import com.github.gpkg4all.common.ModuleInstance
import components.common.extension.ExtensionListComponentProps
import components.common.extension.extensionListComponent
import components.utils.connects
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AppState
import reducer.RemoveExtension
import redux.RAction
import redux.WrapperAction

external interface ExtensionListStateProps : RProps {
    var extensions: List<ModuleInstance>
}

external interface ExtensionListDispatchProps : RProps {
    var onRemoveExtension: (ModuleInstance) -> (Event) -> Unit
}

private val mapStateToProps: ExtensionListStateProps.(AppState, RProps) -> Unit = { state, _ ->
    extensions = state.project.extensions
}

private val mapDispatchToProps: ExtensionListDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit =
    { dispatch, _ ->
        onRemoveExtension = { item ->
            { event: Event ->
                event.preventDefault()
                dispatch(RemoveExtension(item))
            }
        }
    }

private val options: Options<AppState, RProps, ExtensionListStateProps, ExtensionListComponentProps>.() -> Unit =
    {}

fun RBuilder.extensionList(handler: RHandler<RProps> = {}) =
    rConnect(mapStateToProps, mapDispatchToProps, options).connects(
        displayName = "ExtensionList",
        component = extensionListComponent
    )(handler)