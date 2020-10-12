package components.common.explore

import com.github.gpkg4all.common.File
import com.github.gpkg4all.common.FileItem
import com.github.gpkg4all.common.RootFileTree
import com.github.gpkg4all.common.builder
import components.common.builder.button
import connectors.ExploreDialogDispatchProps
import connectors.ExploreDialogProps
import connectors.ExploreDialogStateProps
import kotlinext.js.jsObject
import kotlinx.browser.window
import modules.bodyscrolllock.clearAllBodyScrollLocks
import modules.bodyscrolllock.disableBodyScroll
import modules.react.hotkeys.hotKeys
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.HTMLElement
import react.*
import react.dom.div
import react.dom.strong

interface ExploreDialogComponentProps : ExploreDialogProps, ExploreDialogStateProps, ExploreDialogDispatchProps

val ExploreDialogComponent = functionalComponent<ExploreDialogComponentProps> { props ->

    val wrapper = useRef<HTMLElement?>(null)

    var selected by useState<FileItem?>(null)
    var tree by useState<RootFileTree?>(null)

    useEffect(listOf(props.isShown)) {
        wrapper.current?.focus()
        tree = props.project.spec?.let { spec ->
            builder(spec)
        }
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
                    timeout = 500
                    onExit = {
                        window.setTimeout({
                            tree = null
                            selected = null
                            clearAllBodyScrollLocks()
                        }, timeout = 350)
                    }
                }
                div("explorer") {
                    if (tree != null) {
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
                                            + (props.project.name + ".zip")
                                        }
                                    }
                                    div("explorer-content") {
                                        tree(
                                            selected = selected,
                                            onClickItem = { item -> selected = if (selected == item) null else item },
                                            tree = tree
                                        )
                                    }
                                }
                                div("right") {
                                    div("head") {
                                        div("actions-file") {
                                            +"Some actions"
                                        }
                                    }
                                    div("explorer-content") {
                                        attrs["ref"] = wrapper
                                        val file = selected
                                        if (file is File<*>) {
                                            code(
                                                item = file.unsafeCast<File<Any>>(),
                                                onChange = {}
                                            )
                                        }
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
}


