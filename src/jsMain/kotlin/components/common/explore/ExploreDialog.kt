package components.common.explore

import components.common.builder.button
import connectors.ExploreDialogDispatchProps
import connectors.ExploreDialogProps
import connectors.ExploreDialogStateProps
import kotlinext.js.jsObject
import modules.bodyscrolllock.clearAllBodyScrollLocks
import modules.bodyscrolllock.disableBodyScroll
import modules.react.hotkeys.hotKeys
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.HTMLElement
import react.dom.div
import react.dom.strong
import react.functionalComponent
import react.useEffect
import react.useEffectWithCleanup
import react.useRef

interface ExploreDialogComponentProps : ExploreDialogProps, ExploreDialogStateProps, ExploreDialogDispatchProps

val ExploreDialogComponent = functionalComponent<ExploreDialogComponentProps> { props ->

    val wrapper = useRef<HTMLElement?>(null)

    useEffect {
        wrapper.current?.focus()
    }

    useEffectWithCleanup {
        wrapper.current?.let { htmlElement ->
            if (props.isShown) {
                disableBodyScroll(htmlElement)
            }
        };
        { clearAllBodyScrollLocks() }
    }

    transitionGroup {
        if (props.isShown) {
            cssTransition {
                attrs {
                    classNames = "explorer"
                    timeout = 300
                }
                div("explorer") {
                    hotKeys {
                        attrs {
                            keyMap = jsObject {
                                close = arrayOf("Escape")
                            }
                            handlers = jsObject {
                                close = props.onClose
                            }
                            innerRef = wrapper
                        }
                        div("colset-explorer") {
                            div("left") {
                                div("head") {
                                    strong {
                                        +"{ProjectName}"
                                    }
                                }
                                div("explorer-content") {

                                }
                            }
                            div("right") {
                                div("head") {
                                    div("actions-file") {
                                        +"Some actions"
                                    }
                                }
                                div("explorer-content") {
                                    +"Code"
                                }
                            }
                            div("explorer-actions") {
                                button({
                                    hotkey = "esc"
                                    primary = true
                                    onClick = props.onClose
                                }) {
                                    +"Close"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


