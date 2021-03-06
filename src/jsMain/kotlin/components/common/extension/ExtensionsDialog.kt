package components.common.extension

import com.github.gpkg4all.common.ModuleInstance
import components.common.form.overlay
import components.common.iconEnter
import components.utils.KeyCodes
import components.utils.preventDefault
import components.utils.useWindowsUtils
import kotlinext.js.jsObject
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyDownFunction
import kotlinx.html.js.onKeyUpFunction
import modules.jsearch.Search
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import react.*
import react.dom.*
import react.redux.useDispatch
import react.redux.useSelector
import reducer.AddExtension
import reducer.AppState
import reducer.CloseExtensionsDialog
import redux.RAction
import redux.WrapperAction

external interface ExtensionsDialogProps : RProps

external interface Document {
    var instance: ModuleInstance
    var name: String
    var description: String
    var group: String
}

val extensionsDialogComponent = functionalComponent<ExtensionsDialogProps>("ExtensionsDialog") { _ ->

    val extensions = useSelector { state: AppState -> state.availableExtensions }
    val dispatch = useDispatch<RAction, WrapperAction>()

    val computeGroups = { list: List<ModuleInstance> ->
        list.groupBy { it.module.group }
            .entries
            .sortedBy { it.key }
    }

    val dialog = useRef<HTMLElement?>(null)
    val wrapper = useRef<HTMLElement?>(null)
    val input = useRef<HTMLElement?>(null)
    val windowsUtils = useWindowsUtils()

    var query by useState("")
    var multiple by useState(false)
    var selected by useState(0)
    var groups by useState(computeGroups(extensions))
    var search by useState<Search<Document>?>(null)

    useEffect {
        input.current?.focus()
    }

    useEffect(listOf(extensions)) {
        val newSearch = Search<Document>("name").apply {
            addIndex("name")
            addIndex("description")
            addIndex("group")
        }
        extensions.map {
            jsObject<Document> {
                instance = it
                name = it.module.title
                description = it.module.description ?: ""
                group = it.module.group.title
            }
        }.forEach { newSearch.addDocument(it) }
        search = newSearch
    }

    useEffect(listOf(query)) {
        groups = computeGroups(if (query.isNotEmpty()) {
            search?.search(query.trim())
                ?.map { it.instance } ?: emptyList()
        } else {
            extensions
        })
    }

    val textFocus = { ->
        input.current?.let {
            it.focus()
            it.asDynamic().select()
        }
    }

    val updateScroll = {
        wrapper.current?.let { wrapperElement ->
            val selectedElement = wrapperElement.querySelector("a.selected")?.parentElement.unsafeCast<HTMLElement>()
            val position = selectedElement.offsetTop - wrapperElement.scrollTop
            if (position - 50 < 0 || dialog.current?.clientHeight?.minus(160)?.let { it < position } == true) {
                val top = if (query.isBlank()) 50.0 else 10.0
                wrapperElement.scrollTop = selectedElement.offsetTop - top
            }
        }
    }

    val keyUp = { event: Event ->
        val keyboardEvent = event.asDynamic().nativeEvent.unsafeCast<KeyboardEvent>()
        if (keyboardEvent.keyCode in listOf(91, 93, 17)) {
            multiple = false
        }
    }

    val keyDown = { event: Event ->
        val keyboardEvent = event.asDynamic().nativeEvent.unsafeCast<KeyboardEvent>()
        when (keyboardEvent.keyCode) {
            KeyCodes.K -> if (keyboardEvent.ctrlKey || keyboardEvent.metaKey) {
                dispatch(CloseExtensionsDialog)
            }
            KeyCodes.ARROW_DOWN -> preventDefault(event) {
                groups.flatMap { it.value }.withIndex().drop(selected + 1).find { it.value.valid }
                    ?.let { selected = it.index }
                window.setTimeout(updateScroll)
            }
            KeyCodes.ARROW_UP -> preventDefault(event) { // Up
                groups.flatMap { it.value }.withIndex().take(selected).findLast { it.value.valid }
                    ?.let { selected = it.index }
                window.setTimeout(updateScroll)
            }
            KeyCodes.ENTER -> preventDefault(event) {
                groups.flatMap { it.value }.getOrNull(selected)
                    ?.let { instance ->
                        if (instance.valid) {
                            groups = groups
                                .map { (key, value) -> key to value.filter { it != instance } }
                                .filter { (_, value) -> value.isNotEmpty() }
                                .toMap()
                                .entries
                                .sortedBy { it.key }
                            dispatch(AddExtension(instance))
                        }
                    }
                if (!multiple) {
                    dispatch(CloseExtensionsDialog)
                } else {
                    textFocus()
                }
                Unit
            }
            KeyCodes.ESC -> preventDefault(event) {
                dispatch(CloseExtensionsDialog)
            }
            KeyCodes.TAB -> event.preventDefault()
            KeyCodes.SELECT,
            KeyCodes.COMMAND_CHROME_SAFARI,
            KeyCodes.COMMAND_FIREFOX,
            KeyCodes.COMMAND_OPERA -> multiple = true
            KeyCodes.ARROW_LEFT, KeyCodes.ARROW_RIGHT -> {
            }
            else -> selected = 0
        }
        Unit
    }

    useEffectWithCleanup {
        val clickOutside: (Event) -> Unit = { event: Event ->
            wrapper.current?.let {
                if (!it.contains(event.target as? Node?) && (event.target as? HTMLElement)?.id != "input-quicksearch") {
                    dispatch(CloseExtensionsDialog)
                }
            }
        }

        fun cleanup(clickOutside: (Event) -> Unit): RCleanup = {
            document.removeEventListener("mousedown", clickOutside)
        }

        document.addEventListener("mousedown", clickOutside)
        cleanup(clickOutside)
    }

    var currentIndex = -1
    transitionGroup {
        cssTransition {
            attrs {
                onEnter = {
                    multiple = false
                    query = ""
                    selected = 0
                    groups = computeGroups(extensions)
                    textFocus()
                    Unit
                }
                classNames = "dialog-dependencies"
                timeout = 300
            }
            div("dialog-dependencies") {
                ref = dialog
                div("control-input") {
                    input(classes = "input") {
                        attrs {
                            id = "input-quicksearch"
                            placeholder = "Metadata, Schema, Features, Tiles, ..."
                            onKeyUpFunction = keyUp
                            onKeyDownFunction = keyDown
                            value = query
                            onChangeFunction = { event ->
                                query = event.target.asDynamic().value as String
                                selected = 0
                            }
                        }
                        ref = input
                        attrs["query"] = query
                    }
                    span("help") {
                        +"Press ${windowsUtils.symb} for multiple adds "
                    }
                }
                ul {
                    ref = wrapper
                    groups.forEach { group ->
                        li("group-title") {
                            span {
                                +group.key.title
                            }
                        }
                        group.value.forEach { itemGroup ->
                            val onClick = { event: Event ->
                                event.preventDefault()
                                textFocus()
                                if (itemGroup.valid) {
                                    dispatch(AddExtension(itemGroup))
                                    if (!multiple) {
                                        dispatch(CloseExtensionsDialog)
                                    } else {
                                        groups = groups
                                            .map { (key, value) -> key to value.filter { it != itemGroup } }
                                            .filter { (_, value) -> value.isNotEmpty() }
                                            .toMap()
                                            .entries
                                            .sortedBy { it.key }
                                        selected = -1
                                    }
                                }
                            }
                            li {
                                val selectedLi = ++currentIndex == selected
                                a(
                                    href = "/",
                                    classes = "dependency ${if (selectedLi) "selected" else ""} ${if (!itemGroup.valid) "disabled" else ""}"
                                ) {
                                    if (itemGroup.valid) {
                                        attrs.onClickFunction = onClick
                                    }
                                    strong {
                                        +(itemGroup.module.title + " ")
                                    }
                                    if (itemGroup.module.description != null) {
                                        span("description") {
                                            +itemGroup.module.description
                                        }
                                    }
                                    iconEnter()
                                    if (!itemGroup.valid && itemGroup.invalid != null) {
                                        span("invalid") {
                                            +itemGroup.invalid
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
        open = true
    }
}

@Suppress("FunctionName")
fun RBuilder.ExtensionsDialog(
): Boolean {
    child(extensionsDialogComponent) {

    }
    return true
}

