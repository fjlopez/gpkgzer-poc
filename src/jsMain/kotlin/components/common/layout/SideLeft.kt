package components.common.layout

import components.common.iconGitHub
import components.common.iconTwitter
import components.utils.disableTab
import components.utils.functionalComponent
import kotlinx.html.DIV
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import modules.react.transitiongroup.CssTransitionProps
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import react.*
import react.dom.*

val sideLeftComponent = functionalComponent<RProps>("SideLeft") {
    var open by useState(false)
    var nav by useState(false)
    val wrapper = useRef(null)
    div(if (open) "is-open" else "") {
        attrs.id = "side-left"
        div("side-container") {
            div("navigation-action") {
                button(classes = "hamburger hamburger--spin ${if (nav) "is-active" else ""}") {
                    attrs.onClickFunction = { nav = !nav }
                    span("hamburger-box") {
                        attrs.disableTab()
                        span("hamburger-inner") {}
                    }
                }
            }
            social()
        }
    }
    transitionGroup {
        if (nav) {
            cssTransition {
                attrs.timeout = 500
                attrs.onEnter = { open = true }
                attrs.onExited = { open = false }
                navigationContent(wrapper)
            }
        }
    }
}

private fun RElementBuilder<CssTransitionProps>.navigationContent(wrapper: RMutableRef<Nothing?>) {
    div("navigation") {
        ref = wrapper
        div("navigation-content") {
            div("navigation-content-wrap") {
                header()
                div {
                    ul {
                        li {
                            a {
                                +"Read the adopted GeoPackage version (1.2.1)"
                            }
                        }
                        li {
                            a {
                                +"Read the current GeoPackage working version"
                            }
                        }
                        li {
                            a {
                                +"Find software"
                            }
                        }
                        li {
                            a {
                                +"Find data"
                            }
                        }
                        li {
                            a {
                                +"See the \"Getting Started\" Guide"
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun RDOMBuilder<DIV>.social() {
    div("social") {
        a(target = "_blank", href = "https://github.com/spring-io/start.spring.io") {
            attrs.rel = "noreferrer noopener"
            span("a-content") {
                attrs.disableTab()
                iconGitHub()
            }
        }
        a(target = "_blank", href = "https://github.com/spring-io/start.spring.io") {
            attrs.rel = "noreferrer noopener"
            span("a-content") {
                attrs.disableTab()
                iconTwitter()
            }
        }
    }
}

fun RBuilder.sideLeft() = child(sideLeftComponent) {}
