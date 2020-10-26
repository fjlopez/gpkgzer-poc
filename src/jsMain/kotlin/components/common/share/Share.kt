package components.common.share

import com.github.gpkg4all.common.toDescriptor
import components.common.builder.Button
import components.common.form.overlay
import components.utils.preventDefault
import kotlinext.js.js
import kotlinx.browser.window
import kotlinx.html.id
import kotlinx.html.js.onFocusFunction
import kotlinx.html.js.onKeyDownFunction
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToDynamic
import modules.bodyscrolllock.clearAllBodyScrollLocks
import modules.bodyscrolllock.disableBodyScroll
import modules.querystring.stringify
import modules.react.copytoclipboard.CopyToClipboard
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import react.*
import react.dom.div
import react.dom.h1
import react.dom.input
import react.dom.label
import react.redux.useSelector
import reducer.AppState

external interface ShareProps : RProps {
    var onClose: () -> Unit
}

val shareComponent = functionalComponent<ShareProps>("Share") { props ->

    val project = useSelector { state: AppState -> state.project }
    var button by useState("Copy")
    val input = useRef<HTMLInputElement?>(null)
    val wrapper = useRef<HTMLElement?>(null)

    @Suppress("EXPERIMENTAL_API_USAGE")
    val descriptor = Json.encodeToDynamic(project.toDescriptor())
    val urlToShare = window.location.href + "#" + stringify(descriptor, js { arrayFormat = "bracket" })

    useEffectWithCleanup {
        wrapper.current?.let { htmlElement ->
            disableBodyScroll(htmlElement)
        };
        { clearAllBodyScrollLocks() }
    }

    transitionGroup {
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
                            input(
                                classes = "input${if (button == "Copied!") " padding-lg" else ""}"
                            ) {
                                attrs {
                                    id = "input-share"
                                    onFocusFunction = preventDefault { e: Event ->
                                        when (val target = e.target) {
                                            is HTMLInputElement -> target.select()
                                        }
                                    }
                                    onKeyDownFunction = { e: Event ->
                                        when (e) {
                                            is KeyboardEvent -> if (e.key == "Escape") props.onClose()
                                        }
                                    }
                                    value = urlToShare
                                    readonly = true
                                }
                            }
                            CopyToClipboard {
                                attrs {
                                    text = urlToShare
                                    onCopy = {
                                        button = "Copied!"
                                        input.current?.focus()
                                        window.setTimeout({ props.onClose() }, 1000)
                                    }
                                }
                                Button({
                                    onClick = preventDefault { }
                                }) {
                                    +button
                                }
                            }
                        }
                    }
                    div("popup-action") {
                        Button({
                            hotkey = "ESC"
                            onClick = { props.onClose() }
                        }) {
                            +"Close"
                        }
                    }
                }
            }
        }
    }
    overlay {
        open = true
    }
}

@Suppress("FunctionName")
fun RBuilder.Share(
    onClose: () -> Unit
): Boolean {
    child(shareComponent) {
        attrs.onClose = onClose
    }
    return true
}

