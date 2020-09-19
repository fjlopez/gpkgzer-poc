package components.common.layout

import components.common.Theme
import components.common.Theme.DARK
import components.common.Theme.LIGHT
import components.common.iconMoon
import components.common.iconSun
import components.utils.disableTab
import components.utils.invoke
import kotlinx.html.DIV
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.RDOMBuilder
import react.dom.a
import react.dom.div
import react.dom.span
import react.redux.rConnect
import reducer.*
import redux.RAction
import redux.WrapperAction

interface SideRightComponentProps : SideRightStateProps, SideRightDispatchProps

val sideRightComponent = functionalComponent<SideRightComponentProps> { props ->
    div {
        attrs.id = "side-right"
        div("side-container") {
            themeSwitcher(props)
        }
    }
}


private fun RDOMBuilder<DIV>.themeSwitcher(props: SideRightComponentProps) {
    div("switch") {
        setLightThemeButton(props)
        setDarkThemeButton(props)
    }
}

private fun RDOMBuilder<DIV>.setDarkThemeButton(props: SideRightComponentProps) {
    a(href = "/", classes = "button inverse ${if (props.theme == DARK) "active" else ""}") {
        attrs.onClickFunction = {
            it.preventDefault()
            props.onUpdateTheme(DARK)
        }
        span(classes = "button-content") {
            attrs.disableTab()
            iconMoon()
        }
    }
}

private fun RDOMBuilder<DIV>.setLightThemeButton(props: SideRightComponentProps) {
    a(href = "/", classes = "button inverse top ${if (props.theme == LIGHT) "active" else ""}") {
        attrs.onClickFunction = {
            it.preventDefault()
            props.onUpdateTheme(LIGHT)
        }
        span(classes = "button-content") {
            attrs.disableTab()
            iconSun()
        }
    }
}

external interface SideRightStateProps : RProps {
    var theme: Theme
}

external interface SideRightDispatchProps : RProps {
    var onUpdateTheme : (Theme) -> Unit
}

fun RBuilder.sideRight(handler: RHandler<RProps> = {}) =
    rConnect<State, RAction, WrapperAction, RProps, SideRightStateProps, SideRightDispatchProps, SideRightComponentProps>(
        { state, _ ->
            theme = state.theme
        },
        { dispatch, _ ->
            onUpdateTheme = { dispatch(UpdateTheme(it)) }
        }
    ).invoke(sideRightComponent, "SideRight").invoke(handler)
