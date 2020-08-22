package components.common.layout

import components.utils.addDefaults
import react.RBuilder
import react.RProps
import react.child
import react.dom.*
import react.functionalComponent

private val headerComponent = functionalComponent<RProps> {
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

fun RBuilder.header() = child(addDefaults(headerComponent, "Header")) {
}
