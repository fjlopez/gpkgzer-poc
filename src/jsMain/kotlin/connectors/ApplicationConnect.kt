package connectors

import components.common.ApplicationComponentProps
import components.common.Theme
import components.common.applicationComponent
import components.utils.invoke
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
    var onEscape: () -> Unit
}

private val mapStateToProps: ApplicationStateProps.(AppState, RProps) -> Unit = { state, _ ->
    theme = state.theme
}

private val mapDispatchToProps: ApplicationDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = { dispatch, _ ->
    onEscape = { dispatch(CloseExtensions) }
}

private val options: Options<AppState, ApplicationProps, ApplicationStateProps, ApplicationComponentProps>.() -> Unit =
    {}

fun RBuilder.app(handler: RHandler<ApplicationProps> = {}) =
    rConnect(mapStateToProps, mapDispatchToProps, options)(applicationComponent, "Application")(handler)
