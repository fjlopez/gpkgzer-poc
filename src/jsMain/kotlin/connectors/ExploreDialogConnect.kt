package connectors

import components.common.explore.ExploreDialogComponent
import components.common.explore.ExploreDialogComponentProps
import components.utils.invoke
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AppState
import reducer.CloseExplorer
import redux.RAction
import redux.WrapperAction

external interface ExploreDialogProps : RProps {
}

external interface ExploreDialogStateProps : RProps {
    var isShown: Boolean
}

external interface ExploreDialogDispatchProps : RProps {
    var onClose: (Event) -> Unit
}

private val mapStateToProps: ExploreDialogStateProps.(AppState, RProps) -> Unit = { state, _ ->
    isShown = state.showExplorerDialog
}

private val mapDispatchToProps: ExploreDialogDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit =
    {dispatch, _ ->
        onClose = { event: Event ->
            event.preventDefault()
            dispatch(CloseExplorer)
        }
    }

private val options: Options<AppState, ExploreDialogProps, ExploreDialogStateProps, ExploreDialogComponentProps>.() -> Unit =
    {
    }

fun RBuilder.exploreDialog(handler: RHandler<ExploreDialogProps> = {}) =
    rConnect(mapStateToProps, mapDispatchToProps, options)(ExploreDialogComponent, "ExploreDialog")(handler)