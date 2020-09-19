package reducer

import com.github.gpkg4all.common.*
import components.common.Theme
import config.Configuration
import kotlinext.js.Object
import kotlinext.js.asJsObject
import kotlinext.js.assign
import modules.react.toastify.warning
import redux.*

data class State(
    val theme: Theme = Configuration.theme,
    val project: Project = Project(
        outputTarget = Configuration.supportedTargets.find { it.default },
        spec = Configuration.supportedSpecifications.find { it.default },
        content = Configuration.supportedContents.find { it.default },
        options = Configuration.options.filter { it.default },
        extensions = Configuration.supportedExtensions.filter { it.default }
    ),
    val showExtensionsDialog: Boolean = false,
    val availableExtensions: List<ModuleInstance> = Configuration.supportedExtensions.filter { !it.default }
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

object Reducers {
    val stateReducer = { state: State, action: RAction ->
        when (action) {
            is UpdateTheme -> state.copy(theme = action.theme)
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
            is ShowExtensions -> state.copy(showExtensionsDialog = true)
            is CloseExtensions -> state.copy(showExtensionsDialog = false)
            else -> state
        }
    }

    val validateState = { state: State, _: RAction ->
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
    State(),
    rEnhancer()
)

fun <S> rEnhancer(): Enhancer<S, Action, Action, RAction, WrapperAction> = { next ->
    { reducer, initialState ->
        fun wrapperReducer(reducer: Reducer<S, RAction>): Reducer<S, WrapperAction> {
            return { state, action ->
                reducer(state, (action.asDynamic().action ?: action).unsafeCast<RAction>())
            }
        }

        val store = (next.unsafeCast<StoreCreator<S, WrapperAction, WrapperAction>>())(wrapperReducer(reducer), initialState)
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
