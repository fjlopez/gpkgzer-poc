package components.common.layout

import components.utils.functionalComponent
import react.RBuilder
import react.RProps
import react.child
import react.dom.*

private val headerComponent = functionalComponent<RProps>("Header") {
    header {
        attrs["id"] = "header"
        div("not-mobile") {
            h1("logo") {
                a("/") {
                    span("logo-content") {
                        attrs["tabIndex"] = "-1"
                        logo()
                    }
                }
            }
        }
    }
}

fun RBuilder.header() = child(headerComponent) {
}
