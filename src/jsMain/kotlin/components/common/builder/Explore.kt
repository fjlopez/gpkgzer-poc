package components.common.builder

import components.utils.functionalComponent
import connectors.ExploreDispatchProps
import connectors.ExploreProps
import connectors.ExploreStateProps

external interface ExploreComponentProps : ExploreProps, ExploreStateProps, ExploreDispatchProps

val exploreComponent = functionalComponent<ExploreComponentProps>("Generate") { props ->
    Button({
        id = "explore-project"
        hotkey = "ctrl + space"
        primary = true
        refButton = props.refButton
        onClick = props.onClick
    }) {
        +"Explore"
    }
}

