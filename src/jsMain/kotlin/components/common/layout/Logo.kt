package components.common.layout

import react.RBuilder
import react.RProps
import react.child
import react.dom.img
import react.functionalComponent

private val logoComponent = functionalComponent<LogoComponentProps> { props ->
    img(src = "./images/geopkg.png", classes = props.className) {
        attrs {
            height = "60px" // TODO to remove
        }
    }
}

interface LogoComponentProps : RProps {
    var className: String?
}

fun RBuilder.logo(className: String? = null) = child(logoComponent) {
    attrs.className = className
}

