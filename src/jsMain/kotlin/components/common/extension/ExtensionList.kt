package components.common.extension

import com.github.gpkg4all.common.ModuleInstance
import components.common.iconRemove
import components.utils.disableTab
import connectors.ExtensionListDispatchProps
import connectors.ExtensionListStateProps
import kotlinx.html.LI
import kotlinx.html.js.onClickFunction
import modules.react.transitiongroup.cssTransition
import modules.react.transitiongroup.transitionGroup
import react.dom.*
import react.functionalComponent

interface ExtensionListComponentProps : ExtensionListStateProps, ExtensionListDispatchProps

val extensionListComponent = functionalComponent<ExtensionListComponentProps> { props ->
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
                        item(props, item)
                    }
                }
            }
        }
    }
}

internal fun RDOMBuilder<LI>.item(props: ExtensionListComponentProps, item: ModuleInstance) {
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
                attrs.onClickFunction = props.onRemoveExtension(item)
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

