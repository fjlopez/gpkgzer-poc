package connectors

import com.github.gpkg4all.common.Project
import components.common.builder.ExploreComponentProps
import components.common.builder.exploreComponent
import components.utils.invoke
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.RMutableRef
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AppState
import reducer.ShowExplorer
import redux.RAction
import redux.WrapperAction

external interface ExploreProps : RProps {
    var refButton: RMutableRef<HTMLElement?>
}

external interface ExploreStateProps : RProps {
    var project: Project
}

external interface ExploreDispatchProps : RProps {
    var onClick: (Event) -> Unit
}

private val mapStateToProps: ExploreStateProps.(AppState, RProps) -> Unit = { state, _ ->
    project = state.project
}

private val mapDispatchToProps: ExploreDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = { dispatch, _ ->
    onClick = { event: Event ->
        event.preventDefault()
        dispatch(ShowExplorer)
    }
}

private val options: Options<AppState, ExploreProps, ExploreStateProps, ExploreComponentProps>.() -> Unit =
    {}

fun RBuilder.explore(handler: RHandler<ExploreProps> = {}) =
    rConnect(mapStateToProps, mapDispatchToProps, options)(exploreComponent, "Explore")(handler)