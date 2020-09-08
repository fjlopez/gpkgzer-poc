package components.common.builder

import com.github.gpkg4all.common.ContentTarget
import com.github.gpkg4all.common.ModuleInstance
import com.github.gpkg4all.common.OutputTarget
import com.github.gpkg4all.common.Spec
import components.common.extension.extension
import components.common.form.checkBoxGroup
import components.common.form.radioGroup
import components.utils.invoke
import kotlinext.js.js
import kotlinx.html.DIV
import org.w3c.dom.HTMLElement
import react.RClass
import react.RMutableRef
import react.RProps
import react.dom.RDOMBuilder
import react.dom.div
import react.functionalComponent
import react.redux.rConnect
import reducer.*


interface FieldsProps : RProps {
    var selectedSpec: Spec?
    var availableSpecs: List<Spec>
    var selectedTarget: OutputTarget?
    var availableTargets: List<OutputTarget>
    var selectedContent: ContentTarget?
    var availableContents: List<ContentTarget>
    var selectedOptions: List<ModuleInstance>
    var availableOptions: List<ModuleInstance>
    var refExtension: RMutableRef<HTMLElement?>
    var refGenerate: RMutableRef<HTMLElement?>
}

val fieldsComponent = functionalComponent<FieldsProps> { props ->
    div("colset colset-main") {
        div("left") {
            div("col-sticky") {
                conformanceField(props)
                optionsField(props)
                contentField(props)
                packagingField(props)
            }
        }
        div("right") {
            extension {
                refButton = props.refExtension
            }
        }
    }
    div("actions") {
        div("actions-container") {
            generate {
                refButton = props.refGenerate
            }
        }
    }
}


private fun RDOMBuilder<DIV>.packagingField(props: FieldsProps) {
    control("Packaging") {
        radioGroup<OutputTarget> {
            name = "packaging"
            selected = props.selectedTarget
            options = props.availableTargets
            onChange = { if (props.selectedTarget != it) store.dispatch(UpdateProjectTarget(it)) }
            asText = { it.description }
        }
    }
}

private fun RDOMBuilder<DIV>.contentField(props: FieldsProps) {
    control("GeoPackage content") {
        radioGroup<ContentTarget> {
            name = "content"
            selected = props.selectedContent
            options = props.availableContents
            onChange = { if (props.selectedContent != it) store.dispatch(UpdateProjectContent(it)) }
            asText = { it.description }
        }
    }
}

private fun RDOMBuilder<DIV>.conformanceField(props: FieldsProps) {
    control("GeoPackage conformance") {
        radioGroup<Spec> {
            name = "conformance"
            selected = props.selectedSpec
            options = props.availableSpecs
            onChange = { if (props.selectedSpec != it) store.dispatch(UpdateProjectSpecification(it)) }
            asText = { it.description }
        }
    }
}

private fun RDOMBuilder<DIV>.optionsField(props: FieldsProps) {
    control("GeoPackage options") {
        checkBoxGroup<ModuleInstance> {
            name = "options"
            selected = props.selectedOptions
            options = props.availableOptions
            onChange = { store.dispatch(ToggleProjectOption(it)) }
            asText = { it.module.title }
        }
    }
}

val fields: RClass<FieldsProps> = rConnect<State, RProps, FieldsProps>({ state, _ ->
    selectedSpec = state.project.spec
    selectedTarget = state.project.outputTarget
    selectedContent = state.project.content
    selectedOptions = state.project.options
})(fieldsComponent, "Fields", js {
    availableSpecs = emptyList<Spec>()
    availableTargets = emptyList<OutputTarget>()
    availableContents = emptyList<ContentTarget>()
    availableOptions = emptyList<ModuleInstance>()
}.unsafeCast<FieldsProps>())

