package components.common.extension

import components.common.form.overlay
import components.common.iconEnter
import components.utils.functionalComponent
import components.utils.invoke
import kotlinx.browser.document
import kotlinx.html.js.onClickFunction
import model.ModuleInstance
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.redux.rConnect
import reducer.AddExtension
import reducer.State
import reducer.store

interface ExtensionDialogProps : RProps {
    var onClose: (Event) -> Unit
    var isShown: Boolean
    var extensions: List<ModuleInstance>
}

val extensionDialogComponent = functionalComponent<ExtensionDialogProps>("ExtensionDialog") { props ->
    val dialog = useRef(null)
    val wrapper = useRef<HTMLElement?>(null)

    useEffectWithCleanup {
        val clickOutside: (Event) -> Unit = { event: Event ->
            wrapper.current?.let {
                if (!it.contains(event.target as? Node?) && (event.target as? HTMLElement)?.id != "input-quicksearch") {
                    props.onClose(event)
                }
            }
        }

        fun cleanup(clickOutside: (Event) -> Unit): RCleanup = {
            document.removeEventListener("mousedown", clickOutside)
        }

        document.addEventListener("mousedown", clickOutside)
        cleanup(clickOutside)
    }

    transitionGroup {
        if (props.isShown) {
            cssTransition {
                attrs {
                    classNames = "dialog-dependencies"
                    timeout = 300
                }
                div("dialog-dependencies") {
                    ref = dialog
                    ul {
                        ref = wrapper
                        props.extensions.forEach { item ->
                            val onClick = { event: Event ->
                                event.preventDefault()
                                if (item.valid) {
                                    store.dispatch(AddExtension(item))
                                    props.onClose(event)
                                }
                            }
                            li {
                                a(
                                    href = "/",
                                    classes = "dependency ${if (!item.valid) "disabled" else ""}"
                                ) {
                                    if (item.valid) {
                                        attrs.onClickFunction = onClick
                                    }
                                    strong {
                                        +(item.module.title + " ")
                                        span("group") {
                                            +item.module.group
                                        }
                                    }
                                    if (item.module.description != null) {
                                        span("description") {
                                            +item.module.description
                                        }
                                    }
                                    iconEnter()
                                    if (!item.valid && item.invalid != null) {
                                        span("invalid") {
                                            +item.invalid
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    overlay {
        open = props.isShown
    }
}

val extensionDialog: RClass<ExtensionDialogProps> = rConnect<State, RProps, ExtensionDialogProps>({ state, _ ->
    isShown = state.showExtensionsDialog
    extensions = state.availableExtensions
})(extensionDialogComponent)
