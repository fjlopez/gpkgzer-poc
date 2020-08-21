package components.common.builder

import components.common.form.checkBoxGroup
import components.common.form.radioGroup
import kotlinx.html.DIV
import model.ContentTarget
import model.Module
import model.OutputTarget
import model.Spec
import react.*
import react.dom.RDOMBuilder
import react.dom.div
import react.redux.rConnect
import reducer.*

class Fields : RComponent<FieldsProps, RState>() {
    override fun RBuilder.render() {
        div("colset colset-main") {
            div("left") {
                div("col-sticky") {
                    conformanceField()
                    optionsField()
                    contentField()
                    packagingField()
                }
            }
            div("right") {

            }
        }
    }

    private fun RDOMBuilder<DIV>.packagingField() {
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

    private fun RDOMBuilder<DIV>.contentField() {
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

    private fun RDOMBuilder<DIV>.conformanceField() {
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

    private fun RDOMBuilder<DIV>.optionsField() {
        control("GeoPackage options") {
            checkBoxGroup<Module> {
                name = "options"
                selected = props.selectedOptions
                options = props.availableOptions
                onChange = { store.dispatch(ToggleProjectOption(it)) }
                asText = { it.description }
            }
        }
    }
}

class FieldsProps : RProps {
    var selectedSpec: Spec? = null
    var availableSpecs: List<Spec> = emptyList()
    var selectedTarget: OutputTarget? = null
    var availableTargets: List<OutputTarget> = emptyList()
    var selectedContent: ContentTarget? = null
    var availableContents: List<ContentTarget> = emptyList()
    var selectedOptions: List<Module> = emptyList()
    var availableOptions: List<Module> = emptyList()
}

val fields: RClass<FieldsProps> = rConnect<State, RProps, FieldsProps>({ state, _ ->
    selectedSpec = state.project.spec
    selectedTarget = state.project.outputTarget
    selectedContent = state.project.content
    selectedOptions = state.project.options
})(Fields::class.rClass)
