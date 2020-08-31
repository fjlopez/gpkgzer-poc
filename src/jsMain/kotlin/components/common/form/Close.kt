package components.common.form

import components.common.iconTime
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a

class Close : RComponent<CloseProps, RState>() {
    override fun RBuilder.render() {
        a("toast-close") {
            attrs {
                href = "/#"
                onClickFunction = { event ->
                    event.preventDefault()
                    props.onClose.invoke()
                }
            }
            iconTime()
        }
    }
}

external interface CloseProps : RProps {
    var onClose: () -> Unit
}

fun RBuilder.close(handler: CloseProps.() -> Unit) = child(Close::class) {
    attrs.handler()
}

