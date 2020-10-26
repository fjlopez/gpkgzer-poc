package components.common.explore

import builders.downloadZip
import com.github.gpkg4all.common.File
import com.github.gpkg4all.common.FileItem
import com.github.gpkg4all.common.builder
import components.common.builder.Button
import components.utils.downloadFile
import components.utils.useWindowsUtils
import kotlinext.js.jsObject
import kotlinx.browser.window
import modules.bodyscrolllock.clearAllBodyScrollLocks
import modules.bodyscrolllock.disableBodyScroll
import modules.react.hotkeys.hotKeys
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.*
import react.dom.div
import react.dom.strong
import react.redux.useSelector
import reducer.AppState

external interface ExploreProps : RProps {
    var onClose: (Event) -> Unit
}

val exploreComponent = functionalComponent<ExploreProps>("Explore") { props ->

    val wrapper = useRef<HTMLElement?>(null)

    val project = useSelector { state: AppState -> state.project }
    var selected by useState<FileItem?>(null)
    var tree by useState(project.spec?.let { spec ->
        builder(spec)
    })
    val windowsUtils = useWindowsUtils()

    useEffectWithCleanup {
        wrapper.current?.let { htmlElement ->
            disableBodyScroll(htmlElement)
        };
        { clearAllBodyScrollLocks() }
    }

    transitionGroup {
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
                                        +(project.name + ".zip")
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
                                        Button({
                                            primary = true
                                            onClick = { event: Event ->
                                                event.preventDefault()
                                                (selected as? File<*>)?.let {
                                                    val file = it.unsafeCast<File<Any>>()
                                                    downloadFile(
                                                        filename = file.filename,
                                                        mimetype = "text/plain;charset=utf-8",
                                                        content = file.asText(file.content)
                                                    )
                                                }
                                            }
                                            disabled = selected !is File<*>
                                        }) {
                                            +"Download"
                                        }
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
                                Button({
                                    hotkey = "${windowsUtils.symb} + ⏎"
                                    primary = true
                                    onClick = { event: Event ->
                                        event.preventDefault()
                                        downloadZip(project)
                                    }
                                }) {
                                    +"Download"
                                }
                                Button({
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

@Suppress("FunctionName")
fun RBuilder.Explore(
    onClose: (Event) -> Unit
): Boolean {
    child(exploreComponent) {
        attrs.onClose = onClose
    }
    return true
}

