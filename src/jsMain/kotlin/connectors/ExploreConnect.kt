package connectors

import com.github.gpkg4all.common.Project
import components.common.explore.ExploreComponentProps
import components.common.explore.exploreComponent
import components.utils.connects
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AppState
import redux.RAction
import redux.WrapperAction

external interface ExploreProps : RProps {
    var open: Boolean
    var onClose: (Event) -> Unit
}

external interface ExploreStateProps : RProps {
    var project: Project
}

external interface ExploreDispatchProps : RProps

private val mapStateToProps: ExploreStateProps.(AppState, RProps) -> Unit = { state, _ ->
    project = state.project
}

private val mapDispatchToProps: ExploreDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit =
    { _, _ -> }

private val options: Options<AppState, ExploreProps, ExploreStateProps, ExploreComponentProps>.() -> Unit =
    {}

@Suppress("FunctionName")
fun RBuilder.Explore(show: Boolean, onClose: (Event) -> Unit) =
    rConnect(mapStateToProps, mapDispatchToProps, options).connects(
        displayName = "Explore",
        component = exploreComponent
    ).invoke {
        attrs.open = show
        attrs.onClose = onClose
    }