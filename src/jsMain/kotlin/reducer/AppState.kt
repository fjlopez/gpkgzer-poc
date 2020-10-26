package reducer

import com.github.gpkg4all.common.*
import config.Configuration
import kotlinext.js.Object
import kotlinext.js.assign
import modules.react.toastify.warning
import redux.*

data class AppState(
    val project: Project = Project(
        outputTarget = Configuration.supportedTargets.find { it.default },
        spec = Configuration.supportedSpecifications.find { it.default },
        content = Configuration.supportedContents.find { it.default },
        options = Configuration.supportedOptions.filter { it.default },
        extensions = Configuration.supportedExtensions.filter { it.default },
        name = "sample"
    ),
    val showExtensionsDialog: Boolean = false,
    val availableExtensions: List<ModuleInstance> = Configuration.supportedExtensions.filter { !it.default }
)

class UpdateProjectSpecification(val spec: Spec) : RAction
class UpdateProjectTarget(val outputTarget: OutputTarget) : RAction
class UpdateProjectContent(val target: ContentTarget) : RAction
class UpdateProjectName(val name: String) : RAction
class ToggleProjectOption(val target: ModuleInstance) : RAction
class RemoveExtension(val target: ModuleInstance) : RAction
class AddExtension(val target: ModuleInstance) : RAction
object ShowExtensionsDialog : RAction
object CloseExtensionsDialog : RAction
class LoadExternalConfiguration(val descriptor: ProjectDescriptor) : RAction

object Reducers {
    val stateReducer = { state: AppState, action: RAction ->
        when (action) {
            is LoadExternalConfiguration -> {
                val project = Project(
                    outputTarget = Configuration.supportedTargets.find { it.key == action.descriptor.outputTarget },
                    spec = Configuration.supportedSpecifications.find { it.key == action.descriptor.spec },
                    content = Configuration.supportedContents.find { it.key == action.descriptor.content },
                    options = Configuration.supportedOptions.filter { it.module.key in action.descriptor.options },
                    extensions = Configuration.supportedExtensions.filter { it.module.key in action.descriptor.extensions },
                    name = action.descriptor.name
                )
                state.copy(project = project)
            }
            is UpdateProjectSpecification -> {
                when {
                    action.spec.deprecated -> warning("Version ${action.spec.description} is a deprecated version")
                    action.spec.development -> warning("Version ${action.spec.description} is a development version")
                }
                state.copy(project = updateSpec(state.project, action.spec))
            }
            is UpdateProjectTarget -> state.copy(project = updateOutputTarget(state.project, action.outputTarget))
            is UpdateProjectContent -> state.copy(project = updateContent(state.project, action.target))
            is ToggleProjectOption -> state.copy(project = toggleOption(state.project, action.target))
            is RemoveExtension ->
                state.copy(
                    project = removeExtension(state.project, action.target),
                    availableExtensions = state.availableExtensions + action.target
                )
            is AddExtension ->
                state.copy(
                    project = addExtension(state.project, action.target),
                    availableExtensions = state.availableExtensions - action.target
                )
            is ShowExtensionsDialog -> state.copy(showExtensionsDialog = true)
            is CloseExtensionsDialog -> state.copy(showExtensionsDialog = false)
            is UpdateProjectName -> state.copy(project = state.project.copy(name = action.name))
            else -> state
        }
    }

    val validateState = { state: AppState, _: RAction ->
        state.copy(
            project = updateExtensionValidity(state.project),
            availableExtensions = validateExtensions(state.project, state.availableExtensions)
        )
    }

    operator fun <S, A> Reducer<S, A>.plus(other: Reducer<S, A>): Reducer<S, A> = { state: S, action: A ->
        other.invoke(invoke(state, action), action)
    }
}


val store = createStore(
    with(Reducers) { stateReducer + validateState },
    AppState(),
    rEnhancer()
)

fun <S> rEnhancer(): Enhancer<S, Action, Action, RAction, WrapperAction> = { next ->
    { reducer, initialState ->
        fun wrapperReducer(reducer: Reducer<S, RAction>): Reducer<S, WrapperAction> {
            return { state, action ->
                reducer(state, (action.asDynamic().action ?: action).unsafeCast<RAction>())
            }
        }

        val store =
            (next.unsafeCast<StoreCreator<S, WrapperAction, WrapperAction>>())(wrapperReducer(reducer), initialState)
        val dispatch = { action: RAction ->
            val result = store.dispatch(kotlinext.js.js {
                type = action::class.simpleName
                this.action = action
            }.unsafeCast<WrapperAction>())
            result
        }
        val replaceReducer = { nextReducer: Reducer<S, RAction> ->
            store.replaceReducer(wrapperReducer(nextReducer))
        }
        assign(Object.assign(kotlinext.js.js {}, store)) {
            this.dispatch = dispatch
            this.replaceReducer = replaceReducer
        }.unsafeCast<Store<S, RAction, WrapperAction>>()
    }
}
