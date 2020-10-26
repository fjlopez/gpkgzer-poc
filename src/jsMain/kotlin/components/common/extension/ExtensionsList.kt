package components.common.extension

import com.github.gpkg4all.common.ModuleInstance
import components.common.iconRemove
import components.utils.disableTab
import components.utils.preventDefault
import kotlinx.html.LI
import kotlinx.html.js.onClickFunction
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import react.RBuilder
import react.RProps
import react.child
import react.dom.*
import react.functionalComponent
import react.redux.useDispatch
import react.redux.useSelector
import reducer.AppState
import reducer.RemoveExtension
import redux.WrapperAction

external interface ExtensionsListProps : RProps

val extensionsListComponent = functionalComponent<ExtensionsListProps>("ExtensionsList") { _ ->
    val extensions = useSelector { state: AppState -> state.project.extensions }

    if (extensions.isEmpty()) {
        div("no-dependency") {
            +"No extension selected"
        }
    } else {
        transitionGroup {
            attrs {
                component = "ul"
                className = "dependencies-list"
            }
            extensions.forEach { item ->
                cssTransition {
                    attrs {
                        timeout = 300
                        classNames = "fade"
                    }
                    li {
                        item(item)
                    }
                }
            }
        }
    }
}

internal fun RDOMBuilder<LI>.item(item: ModuleInstance) {
    val dispatch = useDispatch<RemoveExtension, WrapperAction>()
    div("dependency-item ${if (!item.valid) "disabled" else ""}") {
        strong {
            +(item.module.title + " ")
            span("group") {
                +item.module.group.title
            }
        }
        if (item.valid && item.module.description != null) {
            span("description") {
                +item.module.description
            }
        }
        a(href = "", classes = "icon") {
            span("a-content") {
                attrs.disableTab()
                attrs.onClickFunction = preventDefault { dispatch(RemoveExtension(item)) }
                iconRemove()
            }
        }
        if (!item.valid && item.invalid != null) {
            span("invalid") {
                +item.invalid
            }
        }
    }
}

@Suppress("FunctionName")
fun RBuilder.ExtensionsList(
): Boolean {
    child(extensionsListComponent) {
    }
    return true
}

