package reducer

import components.common.Theme
import config.Configuration
import model.*
import modules.react.toastify.warning
import redux.RAction
import redux.createStore
import redux.rEnhancer

data class State(
    val theme: Theme = Configuration.theme,
    val project: Project = Project(
        outputTarget = Configuration.supportedTargets.find { it.default },
        spec = Configuration.supportedSpecifications.find { it.default },
        content = Configuration.supportedContents.find { it.default },
        options = Configuration.options.filter { it.default }
    )
)

class UpdateTheme(val theme: Theme) : RAction
class UpdateProjectSpecification(val spec: Spec) : RAction
class UpdateProjectTarget(val outputTarget: OutputTarget) : RAction
class UpdateProjectContent(val target: ContentTarget) : RAction
class ToggleProjectOption(val target: Module) : RAction

val stateReducer = { state: State, action: RAction ->
    when (action) {
        is UpdateTheme -> state.copy(theme = action.theme)
        is UpdateProjectSpecification -> {
            when {
                action.spec.deprecated -> warning("Version ${action.spec.description} is a deprecated version")
                action.spec.development -> warning("Version ${action.spec.description} is a development version")
            }
            state.copy(project = state.project.copy(spec = action.spec))
        }
        is UpdateProjectTarget -> state.copy(project = state.project.copy(outputTarget = action.outputTarget))
        is UpdateProjectContent -> state.copy(project = state.project.copy(content = action.target))
        is ToggleProjectOption -> {
            val newOptions = if (action.target in state.project.options)
                state.project.options - action.target
            else
                state.project.options + action.target
            state.copy(project = state.project.copy(options = newOptions))
        }
        else -> state
    }
}

val store = createStore(
    stateReducer, State(), rEnhancer()
)
