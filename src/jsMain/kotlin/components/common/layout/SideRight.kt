package components.common.layout

import components.common.Theme
import components.common.Theme.DARK
import components.common.Theme.LIGHT
import components.common.iconMoon
import components.common.iconSun
import components.utils.disableTab
import components.utils.preventDefault
import kotlinx.browser.document
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.a
import react.dom.div
import react.dom.span

external interface SideRightProps : RProps {
    var theme: Theme
}

val sideRightComponent = functionalComponent<SideRightProps>("SideRight") { props ->

    var theme by useState(props.theme)

    document.body?.className = theme.className

    fun activeWhen(target: Theme) = if (theme == target) "active" else ""
    fun setTheme(target: Theme) = preventDefault { theme = target }

    div {
        attrs.id = "side-right"
        div("side-container") {
            div("switch") {
                a(href = "/", classes = "button inverse top ${activeWhen(LIGHT)}") {
                    attrs.onClickFunction = setTheme(LIGHT)
                    span(classes = "button-content") {
                        attrs.disableTab()
                        iconSun()
                    }
                }
                a(href = "/", classes = "button inverse ${activeWhen(DARK)}") {
                    attrs.onClickFunction = setTheme(DARK)
                    span(classes = "button-content") {
                        attrs.disableTab()
                        iconMoon()
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
fun RBuilder.SideRight(theme: Theme) {
    child(sideRightComponent) {
        attrs {
            this.theme = theme
        }
    }
}
