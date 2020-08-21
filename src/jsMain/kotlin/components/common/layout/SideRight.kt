package components.common.layout

import components.common.Theme
import components.common.Theme.DARK
import components.common.Theme.LIGHT
import components.common.iconMoon
import components.common.iconSun
import kotlinx.html.DIV
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.RDOMBuilder
import react.dom.a
import react.dom.div
import react.dom.span
import react.redux.rConnect
import reducer.State
import reducer.UpdateTheme
import reducer.store


class SideRight : RComponent<SideRightProps, RState>() {
    override fun RBuilder.render() {
        div {
            attrs["id"] = "side-right"
            div("side-container") {
                themeSwitcher(props)
            }
        }
    }
}

interface SideRightProps : RProps {
    var theme: Theme
}


private fun RDOMBuilder<DIV>.themeSwitcher(props: SideRightProps) {
    div("switch") {
        setLightThemeButton(props)
        setDarkThemeButton(props)
    }
}

private fun RDOMBuilder<DIV>.setDarkThemeButton(props: SideRightProps) {
    a(href = "/", classes = "button inverse ${if (props.theme == DARK) "active" else ""}") {
        attrs.onClickFunction = {
            it.preventDefault()
            store.dispatch(UpdateTheme(DARK))
        }
        span(classes = "button-content") {
            attrs["tabIndex"] = -1
            iconMoon()
        }
    }
}

private fun RDOMBuilder<DIV>.setLightThemeButton(props: SideRightProps) {
    a(href = "/", classes = "button inverse top ${if (props.theme == LIGHT) "active" else ""}") {
        attrs.onClickFunction = {
            it.preventDefault()
            store.dispatch(UpdateTheme(LIGHT))
        }
        span(classes = "button-content") {
            attrs["tabIndex"] = -1
            iconSun()
        }
    }
}

val sideRight: RClass<SideRightProps> = rConnect<State, RProps, SideRightProps>({ state, _ ->
    theme = state.theme
})(SideRight::class.rClass)
