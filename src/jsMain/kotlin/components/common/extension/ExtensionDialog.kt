package components.common.extension

import components.common.form.overlay
import components.common.iconEnter
import components.utils.functionalComponent
import components.utils.invoke
import components.utils.useWindowsUtils
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyDownFunction
import kotlinx.html.js.onKeyUpFunction
import model.Group
import model.ModuleInstance
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
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
    fun computeGroups(): List<Map.Entry<Group, List<ModuleInstance>>> = props.extensions.groupBy { it.module.group }
        .entries
        .sortedBy { it.key }

    val dialog = useRef<HTMLElement?>(null)
    val wrapper = useRef<HTMLElement?>(null)
    val input = useRef<HTMLElement?>(null)
    val query = useState("")
    val windowsUtils = useWindowsUtils()

    val multiple = useState(false)
    val selected = useState<Int>(0)
    val groups = useState(computeGroups())


    val textFocus = { ->
        input.current?.let {
            it.focus()
            it.asDynamic().select()
        }
    }

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

    val updateScroll = {
        wrapper.current?.let { wrapperElement ->
            val selectedElement = wrapperElement.querySelector("a.selected")?.parentElement.unsafeCast<HTMLElement>()
            val position = selectedElement.offsetTop - wrapperElement.scrollTop
            if (position - 50 < 0 || dialog.current?.clientHeight?.minus(160)?.let { it < position } == true) {
                // const top = query.trim() === '' ? 50 : 10
                val top = 50.0
                wrapperElement.scrollTop = selectedElement.offsetTop - top
            }
        }
    }

    var currentIndex = -1
    transitionGroup {
        if (props.isShown) {
            cssTransition {
                attrs {
                    onEnter = {
                        multiple.second(false)
                        selected.second(0)
                        groups.second(computeGroups())
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
                                onKeyUpFunction = { event: Event ->
                                    if (event.type == "keyup") {
                                        val keyboardEvent = event.asDynamic().nativeEvent.unsafeCast<KeyboardEvent>()
                                        if (keyboardEvent.keyCode in listOf(91, 93, 17)) {
                                            multiple.second(false)
                                        }
                                    }
                                }
                                onKeyDownFunction = { event: Event ->
                                    if (event.type == "keydown") {
                                        val keyboardEvent = event.asDynamic().nativeEvent.unsafeCast<KeyboardEvent>()
                                        when (keyboardEvent.keyCode) {
                                            75 -> if (keyboardEvent.ctrlKey || keyboardEvent.metaKey) {
                                                props.onClose(event)
                                            }
                                            40 -> { // Down
                                                event.preventDefault()
                                                val target = groups.first.flatMap { it.value }.withIndex()
                                                    .drop(selected.first + 1).find { it.value.valid }
                                                target?.let { selected.second(it.index) }
                                                window.setTimeout(updateScroll)
                                            }
                                            38 -> { // Up
                                                event.preventDefault()
                                                val target =
                                                    groups.first.flatMap { it.value }.withIndex().take(selected.first)
                                                        .findLast { it.value.valid }
                                                target?.let { selected.second(it.index) }
                                                window.setTimeout(updateScroll)
                                            }
                                            13 -> { // Up
                                                event.preventDefault()
                                                groups.first.flatMap { it.value }.getOrNull(selected.first)
                                                    ?.let { instance ->
                                                        if (instance.valid) {
                                                            val updatedGroups = groups.first
                                                                .map { (key, value) -> key to value.filter { it != instance } }
                                                                .filter { (_, value) -> value.isNotEmpty() }
                                                                .toMap()
                                                                .entries
                                                                .sortedBy { it.key }
                                                            groups.second(updatedGroups)
                                                            store.dispatch(AddExtension(instance))
                                                        }
                                                    }
                                                if (!multiple.first) {
                                                    props.onClose(event)
                                                } else {
                                                    textFocus()
                                                }
                                            }
                                            27 -> { // Escape
                                                event.preventDefault()
                                                props.onClose(event)
                                            }
                                            9 -> { // Tab
                                                event.preventDefault()
                                            }
                                            91, 93, 224, 17 -> {
                                                multiple.second(true)
                                            }
                                            39, 37 -> {
                                            } // LEFT, RIGHT
                                            else -> {
                                                selected.second(0)
                                            }
                                        }
                                    }
                                }
                            }
                            ref = input
                            attrs["query"] = query.first
                        }
                        span("help") {
                            +"Press ${windowsUtils.symb} for multiple adds "
                        }
                    }
                    ul {
                        ref = wrapper
                        props.extensions.groupBy { it.module.group }
                            .entries
                            .sortedBy { it.key }
                            .forEach { group ->
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
                                            store.dispatch(AddExtension(itemGroup))
                                            props.onClose(event)
                                        }
                                    }
                                    li {
                                        val selectedLi = ++currentIndex == selected.first
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
    }
    overlay {
        open = props.isShown
    }
}

val extensionDialog: RClass<ExtensionDialogProps> = rConnect<State, RProps, ExtensionDialogProps>({ state, _ ->
    isShown = state.showExtensionsDialog
    extensions = state.availableExtensions
})(extensionDialogComponent)
