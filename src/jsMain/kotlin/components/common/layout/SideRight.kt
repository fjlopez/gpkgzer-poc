package components.common.layout

import components.common.Theme.DARK
import components.common.Theme.LIGHT
import components.common.iconMoon
import components.common.iconSun
import components.utils.disableTab
import connectors.SideRightDispatchProps
import connectors.SideRightStateProps
import kotlinx.html.DIV
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.RDOMBuilder
import react.dom.a
import react.dom.div
import react.dom.span

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

