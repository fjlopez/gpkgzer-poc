package components.common.form

import components.utils.functionalComponent
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import react.RBuilder
import react.RProps
import react.child
import react.dom.div

interface OverlayProps : RProps {
    var open: Boolean
}

private val overlayComponent = functionalComponent<OverlayProps>("Overlay") { props ->
    transitionGroup {
        if (props.open) {
            cssTransition {
                attrs {
                    classNames = "overlay"
                    timeout = 100
                }
                div("overlay") {
                }
            }
        }
    }
}

fun RBuilder.overlay(handler: OverlayProps.() -> Unit) = child(overlayComponent) {
    attrs.handler()
}
