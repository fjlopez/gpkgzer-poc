package components.common.share

import components.common.builder.Button
import components.common.form.overlay
import connectors.ShareDispatchProps
import connectors.ShareProps
import connectors.ShareStateProps
import kotlinx.browser.window
import kotlinx.html.INPUT
import kotlinx.html.id
import kotlinx.html.js.onFocusFunction
import kotlinx.html.js.onKeyDownFunction
import kotlinx.html.select
import modules.bodyscrolllock.clearAllBodyScrollLocks
import modules.bodyscrolllock.disableBodyScroll
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import react.dom.div
import react.dom.h1
import react.dom.input
import react.dom.label
import react.functionalComponent
import react.useEffectWithCleanup
import react.useRef

interface ShareComponentProps : ShareProps, ShareStateProps, ShareDispatchProps

val shareComponent = functionalComponent<ShareComponentProps>("Share") { props ->

    val wrapper = useRef<HTMLElement?>(null)
    val urlToShare = window.location.origin

    useEffectWithCleanup {
        wrapper.current?.let { htmlElement ->
            if (props.open) {
                disableBodyScroll(htmlElement)
            }
        };
        { clearAllBodyScrollLocks() }
    }

    transitionGroup {
        if (props.open) {
            cssTransition {
                attrs {
                    classNames = "popup"
                    timeout = 300
                }
                div("popup-share") {
                    div("popup-share-container") {
                        div("popup-header") {
                            h1 {
                                +"Share your configuration"
                            }
                        }
                        div("popup-content") {
                            label {
                                attrs["htmlFor"] = "input-share"
                                +"""
                                        Use this link to share the current configuration. Attributes
                                        can be removed from the URL if you want to rely on our
                                        defaults.
                                    """.trimIndent()
                            }
                            div("control") {
                                input {
                                    attrs {
                                        id = "input-share"
                                        onFocusFunction = { e: Event ->
                                            e.preventDefault()
                                            e.target.unsafeCast<INPUT>().select()
                                        }
                                        onKeyDownFunction = { e: Event ->
                                            when (e) {
                                                is KeyboardEvent -> if (e.key == "Escape") props.onClose(e)
                                            }
                                        }
                                        value = urlToShare
                                        readonly = true
                                    }
                                }
                            }
                        }
                        div("popup-action") {
                            Button({
                                hotkey = "ESC"
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
    overlay {
        open = props.open
    }
}


