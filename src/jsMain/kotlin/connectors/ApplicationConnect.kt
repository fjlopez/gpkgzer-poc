package connectors

import components.common.ApplicationComponentProps
import components.common.Theme
import components.common.applicationComponent
import components.utils.connects
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AppState
import reducer.CloseExtensions
import redux.RAction
import redux.WrapperAction

external interface ApplicationProps : RProps

external interface ApplicationStateProps : RProps {
    var theme: Theme
}

external interface ApplicationDispatchProps : RProps {
    var onCloseExtensions: (Event) -> Unit
}

private val mapStateToProps: ApplicationStateProps.(AppState, RProps) -> Unit = { state, _ ->
    theme = state.theme
}

private val mapDispatchToProps: ApplicationDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = { dispatch, _ ->
    onCloseExtensions = { event: Event ->
        event.preventDefault()
        dispatch(CloseExtensions)
    }
}

private val options: Options<AppState, ApplicationProps, ApplicationStateProps, ApplicationComponentProps>.() -> Unit =
    {}

fun RBuilder.app(handler: RHandler<ApplicationProps> = {}) =
    rConnect(mapStateToProps, mapDispatchToProps, options).connects(
        displayName = "Application",
        component = applicationComponent
    )(handler)
