package components.common

import components.utils.invoke
import react.RBuilder
import react.RHandler
import react.RProps
import react.redux.rConnect
import reducer.CloseExtensions
import reducer.State
import redux.RAction
import redux.WrapperAction

external interface ApplicationStateProps : RProps {
    var theme: Theme
}

external interface ApplicationDispatchProps : RProps {
    var onEscape: () -> Unit
}

fun RBuilder.app(handler: RHandler<RProps> = {}) =
    rConnect<State, RAction, WrapperAction, RProps, ApplicationStateProps, ApplicationDispatchProps, ApplicationComponentProps>(
        { state, _ -> theme = state.theme },
        { dispatch, _ -> onEscape = { dispatch(CloseExtensions) } }
    ).invoke(applicationComponent, "Application").invoke(handler)
