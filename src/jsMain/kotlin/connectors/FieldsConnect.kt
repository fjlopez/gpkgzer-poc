package connectors

import com.github.gpkg4all.common.ContentTarget
import com.github.gpkg4all.common.ModuleInstance
import com.github.gpkg4all.common.OutputTarget
import com.github.gpkg4all.common.Spec
import components.common.builder.FieldsComponentProps
import components.common.builder.fieldsComponent
import components.utils.invoke
import org.w3c.dom.HTMLElement
import react.RBuilder
import react.RHandler
import react.RMutableRef
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.*
import redux.RAction
import redux.WrapperAction

external interface FieldsProps : RProps {
    var availableSpecs: List<Spec>
    var availableTargets: List<OutputTarget>
    var availableContents: List<ContentTarget>
    var availableOptions: List<ModuleInstance>
    var refExtensions: RMutableRef<HTMLElement?>
    var refGenerate: RMutableRef<HTMLElement?>
    var refExplore: RMutableRef<HTMLElement?>
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

private val mapStateToProps: FieldsStateProps.(AppState, RProps) -> Unit = { state, _ ->
    with(state.project) {
        selectedSpec = spec
        selectedTarget = outputTarget
        selectedContent = content
        selectedOptions = options
    }
}

private val mapDispatchToProps: FieldsDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = { dispatch, _ ->
    onChangeOptions = { dispatch(ToggleProjectOption(it)) }
    onChangeSpecs = { dispatch(UpdateProjectSpecification(it)) }
    onChangeContent = { dispatch(UpdateProjectContent(it)) }
    onChangeTarget = { dispatch(UpdateProjectTarget(it)) }
}

private val options: Options<AppState, FieldsProps, FieldsStateProps, FieldsComponentProps>.() -> Unit =
    {
    }


fun RBuilder.fields(handler: RHandler<FieldsProps> = {}) =
    rConnect(mapStateToProps, mapDispatchToProps, options)(fieldsComponent, "Fields") {
        availableSpecs = emptyList()
        availableTargets = emptyList()
        availableContents = emptyList()
        availableOptions = emptyList()
    }(handler)