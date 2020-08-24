package components.common.extension

import components.common.iconRemove
import components.utils.functionalComponent
import components.utils.invoke
import kotlinx.html.LI
import kotlinx.html.js.onClickFunction
import model.ModuleInstance
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import org.w3c.dom.events.Event
import react.RClass
import react.RProps
import react.dom.*
import react.redux.rConnect
import reducer.RemoveExtension
import reducer.State
import reducer.store

interface ExtensionListProps : RProps {
    var extensions: List<ModuleInstance>
}

val extensionListComponent = functionalComponent<ExtensionListProps>("ExtensionList") { props ->
    if (props.extensions.isEmpty()) {
        div("no-dependency") {
            +"No extension selected"
        }
    } else {
        transitionGroup {
            attrs {
                component = "ul"
                className = "dependencies-list"
            }
            props.extensions.forEach { item ->
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

private fun RDOMBuilder<LI>.item(item: ModuleInstance) {
    div("dependency-item ${if (!item.valid) "disabled" else ""}") {
        strong {
            +(item.module.title + " ")
            span("group") {
                +item.module.group
            }
        }
        if (item.valid && item.module.description != null) {
            span("description") {
                +item.module.description
            }
        }
        a(href = "", classes = "icon") {
            span("a-content") {
                attrs["tabIndex"] = -1
                attrs.onClickFunction = { event: Event ->
                    event.preventDefault()
                    store.dispatch(RemoveExtension(item))
                }
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

val extensionList: RClass<ExtensionListProps> = rConnect<State, RProps, ExtensionListProps>({ state, _ ->
    extensions = state.project.extensions
})(extensionListComponent)
