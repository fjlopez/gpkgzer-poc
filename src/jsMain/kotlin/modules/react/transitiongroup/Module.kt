@file:JsModule("react-transition-group")
@file:JsNonModule

package modules.react.transitiongroup

import react.RClass
import react.RProps

@JsName("TransitionGroup")
external val transitionGroup: RClass<TransitionGroupProps>

external interface TransitionGroupProps : RProps {
    var component: String?
    var className: String?
}

@JsName("CSSTransition")
external val cssTransition: RClass<CssTransitionProps>

external interface CssTransitionProps : RProps {
    var classNames: String?
    var onEnter: () -> Unit
    var onEntered: () -> Unit
    var onExit: () -> Unit
    var onExited: () -> Unit
    var timeout: Number
}
