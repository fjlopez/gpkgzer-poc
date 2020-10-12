package connectors

import com.github.gpkg4all.common.Project
import components.common.builder.GenerateComponentProps
import components.common.builder.generateComponent
import components.utils.connects
import org.w3c.dom.HTMLElement
import react.RBuilder
import react.RHandler
import react.RMutableRef
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AppState

external interface GenerateProps : RProps {
    var refButton: RMutableRef<HTMLElement?>
}

external interface GenerateStateProps : RProps {
    var project: Project
}

private val mapStateToProps: GenerateStateProps.(AppState, RProps) -> Unit = { state, _ ->
    project = state.project
}

private val options: Options<AppState, GenerateProps, GenerateComponentProps, GenerateComponentProps>.() -> Unit =
    {}

fun RBuilder.generate(handler: RHandler<GenerateProps> = {}) =
    rConnect(mapStateToProps, options).connects(
        displayName = "Generate",
        component = generateComponent
    )(handler)