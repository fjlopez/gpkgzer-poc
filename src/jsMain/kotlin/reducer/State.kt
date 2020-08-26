package reducer

import components.common.Theme
import config.Configuration
import model.*
import modules.react.toastify.warning
import redux.RAction
import redux.Reducer
import redux.createStore
import redux.rEnhancer

data class State(
    val theme: Theme = Configuration.theme,
    val project: Project = Project(
        outputTarget = Configuration.supportedTargets.find { it.default },
        spec = Configuration.supportedSpecifications.find { it.default },
        content = Configuration.supportedContents.find { it.default },
        options = Configuration.options.filter { it.default },
        extensions = emptyList()
    ),
    val showExtensionsDialog: Boolean = false,
    val availableExtensions: List<ModuleInstance> = Configuration.supportedExtensions.map { ModuleInstance(it) }
)

class UpdateTheme(val theme: Theme) : RAction
class UpdateProjectSpecification(val spec: Spec) : RAction
class UpdateProjectTarget(val outputTarget: OutputTarget) : RAction
class UpdateProjectContent(val target: ContentTarget) : RAction
class ToggleProjectOption(val target: ModuleInstance) : RAction
class RemoveExtension(val target: ModuleInstance) : RAction
class AddExtension(val target: ModuleInstance) : RAction
object ShowExtensions : RAction
object CloseExtensions : RAction

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
        is RemoveExtension ->
            state.copy(
                project = state.project.copy(extensions = state.project.extensions - action.target),
                availableExtensions = state.availableExtensions + action.target
            )
        is AddExtension ->
            state.copy(
                project = state.project.copy(extensions = state.project.extensions + action.target),
                availableExtensions = state.availableExtensions - action.target
            )
        is ShowExtensions -> state.copy(showExtensionsDialog = true)
        is CloseExtensions -> state.copy(showExtensionsDialog = false)
        else -> state
    }
}

val validateState = { state: State, _: RAction ->
    val currentSpec = state.project.spec
    if (currentSpec != null) {
        val newExtensions = state.project.extensions.validate(state.project)
        val newAvailableExtensions = state.availableExtensions.validate(state.project)
        state.copy(
            project = state.project.copy(extensions = newExtensions),
            availableExtensions = newAvailableExtensions
        )
    } else state
}

private fun List<ModuleInstance>.validate(project: Project): List<ModuleInstance> {
    require(project.spec != null)
    return map {
        val reasons = mutableListOf<String>()
        // Check Spec
        when {
            it.module.deprecatedBy == null && it.module.officialSince > project.spec ->
                reasons += "GeoPackage >= ${it.module.officialSince.key}"
            it.module.deprecatedBy != null && it.module.officialSince > project.spec && it.module.deprecatedBy <= project.spec ->
                reasons += "GeoPackage >= ${it.module.officialSince.key} and < ${it.module.deprecatedBy.key}"
            it.module.deprecatedBy != null && it.module.officialSince <= project.spec && it.module.deprecatedBy <= project.spec ->
                reasons += "GeoPackage < ${it.module.deprecatedBy.key}"
        }
        // Extensions is required
        if (!project.options.any { option -> option.module == Modules.extensions }) {
            reasons += "Extensions option"
        }
        // Some option is required
        if (it.module.dependsOn is AtLeastOneRule) {
            val options = it.module.dependsOn.modules
            if (!project.options.any { option -> option.module in options }) {
                when {
                    options.size > 1 -> {
                        val (head, last) = options.map { option -> option.title }.chunked(options.size - 1)
                        reasons += "at least one of ${head.joinToString(separator = " option, ")} or ${last.first()} option"
                    }
                    options.size == 1 -> {
                        reasons += options.first().title + " option"
                    }
                }
            }
        }
        if (it.module.requireUdt && project.content == ContentTargets.metadata) {
            reasons += "User Tables (example)"
        }
        // Reasons
        when {
            reasons.size > 1 -> {
                val (head, last) = reasons.chunked(reasons.size - 1)
                it.copy(
                    valid = false,
                    invalid = "Requires ${head.joinToString(separator = ", ")}, and ${last.first()}"
                )
            }
            reasons.size == 1 -> {
                it.copy(valid = false, invalid = "Requires ${reasons.first()}")
            }
            !it.valid -> it.copy(valid = true)
            else -> it
        }
    }
}


operator fun <S, A> Reducer<S, A>.plus(other: Reducer<S, A>): Reducer<S, A> = { state: S, action: A ->
    other.invoke(invoke(state, action), action)
}

val store = createStore(
    stateReducer + validateState, State(), rEnhancer()
)
