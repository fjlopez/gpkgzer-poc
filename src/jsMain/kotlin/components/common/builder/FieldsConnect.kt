package components.common.builder

import com.github.gpkg4all.common.ContentTarget
import com.github.gpkg4all.common.ModuleInstance
import com.github.gpkg4all.common.OutputTarget
import com.github.gpkg4all.common.Spec
import components.utils.invoke
import org.w3c.dom.HTMLElement
import react.RBuilder
import react.RHandler
import react.RMutableRef
import react.RProps
import react.redux.rConnect
import reducer.*
import redux.RAction
import redux.WrapperAction

external interface FieldsProps : RProps {
    var availableSpecs: List<Spec>
    var availableTargets: List<OutputTarget>
    var availableContents: List<ContentTarget>
    var availableOptions: List<ModuleInstance>
    var refExtension: RMutableRef<HTMLElement?>
    var refGenerate: RMutableRef<HTMLElement?>
}

external interface FieldsStateProps : RProps {
    var selectedSpec: Spec?
    var selectedTarget: OutputTarget?
    var selectedContent: ContentTarget?
    var selectedOptions: List<ModuleInstance>
}

external interface FieldsDispatchProps : RProps {
    var onChangeOptions: (ModuleInstance) -> Unit
    var onChangeSpecs: (Spec) -> Unit
    var onChangeContent: (ContentTarget) -> Unit
    var onChangeTarget: (OutputTarget) -> Unit
}

fun RBuilder.fields(handler: RHandler<FieldsProps> = {}) =
    rConnect<State, RAction, WrapperAction, FieldsProps, FieldsStateProps, FieldsDispatchProps, FieldsComponentProps>(
        { state, _ ->
            selectedSpec = state.project.spec
            selectedTarget = state.project.outputTarget
            selectedContent = state.project.content
            selectedOptions = state.project.options
        },
        { dispatch, _ ->
            onChangeOptions = { dispatch(ToggleProjectOption(it)) }
            onChangeSpecs = { dispatch(UpdateProjectSpecification(it)) }
            onChangeContent = { dispatch(UpdateProjectContent(it)) }
            onChangeTarget = { dispatch(UpdateProjectTarget(it)) }
        }
    ).invoke(fieldsComponent, "Fields", kotlinext.js.js {
        availableSpecs = emptyList<Spec>()
        availableTargets = emptyList<OutputTarget>()
        availableContents = emptyList<ContentTarget>()
        availableOptions = emptyList<ModuleInstance>()
    }.unsafeCast<FieldsComponentProps>()).invoke(handler)