package components.common.layout

import react.RBuilder
import react.RProps
import react.child
import react.dom.img
import react.functionalComponent

private val logoComponent = { className: String? ->
    functionalComponent<RProps> {
        img(src = "./images/geopkg.png", classes = className) {
            attrs.height = "60px" // TODO to remove
        }
    }
}

fun RBuilder.logo(className: String? = null) = child(logoComponent(className)) {
}

