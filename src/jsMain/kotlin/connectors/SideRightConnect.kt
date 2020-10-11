package connectors

import components.common.Theme
import components.common.layout.SideRightComponentProps
import components.common.layout.sideRightComponent
import components.utils.invoke
import react.RBuilder
import react.RHandler
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AppState
import reducer.UpdateTheme
import redux.RAction
import redux.WrapperAction

external interface SideRightStateProps : RProps {
    var theme: Theme
}

external interface SideRightDispatchProps : RProps {
    var onUpdateTheme: (Theme) -> Unit
}

private val mapStateToProps: SideRightStateProps.(AppState, RProps) -> Unit = { state, _ ->
    theme = state.theme
}

private val mapDispatchToProps: SideRightDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = { dispatch, _ ->
    onUpdateTheme = { dispatch(UpdateTheme(it)) }
}

private val options: Options<AppState, RProps, SideRightStateProps, SideRightComponentProps>.() -> Unit =
    {}

fun RBuilder.sideRight(handler: RHandler<RProps> = {}) =
    rConnect(mapStateToProps, mapDispatchToProps, options)(sideRightComponent, "SideRight")(handler)