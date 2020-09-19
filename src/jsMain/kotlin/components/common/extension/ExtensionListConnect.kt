package components.common.extension

import com.github.gpkg4all.common.ModuleInstance
import components.utils.invoke
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.RProps
import react.redux.rConnect
import reducer.RemoveExtension
import reducer.State
import redux.RAction
import redux.WrapperAction

external interface ExtensionListStateProps : RProps {
    var extensions: List<ModuleInstance>
}

external interface ExtensionListDispatchProps : RProps {
    var onRemoveExtension: (ModuleInstance) -> (Event) -> Unit
}

fun RBuilder.extensionList(handler: RHandler<RProps> = {}) =
    rConnect<State, RAction, WrapperAction, RProps, ExtensionListStateProps, ExtensionListDispatchProps, ExtensionListComponentProps>(
        { state, _ ->
            extensions = state.project.extensions
        },
        { dispatch, _ ->
            onRemoveExtension = { item ->
                { event: Event ->
                    event.preventDefault()
                    dispatch(RemoveExtension(item))
                }
            }
        }
    ).invoke(extensionListComponent, "ExtensionList").invoke(handler)