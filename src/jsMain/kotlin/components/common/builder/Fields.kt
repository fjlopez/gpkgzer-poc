package components.common.builder

import components.common.extension.extension
import components.common.form.checkBoxGroup
import components.common.form.radioGroup
import kotlinext.js.jsObject
import kotlinx.html.DIV
import model.ContentTarget
import model.ModuleInstance
import model.OutputTarget
import model.Spec
import org.w3c.dom.HTMLElement
import react.*
import react.dom.RDOMBuilder
import react.dom.div
import react.redux.rConnect
import reducer.*

private class Fields : RComponent<FieldsProps, RState>() {
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
                extension {
                    attrs {
                        refButton = props.refExtension
                    }
                }
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
            checkBoxGroup<ModuleInstance> {
                name = "options"
                selected = props.selectedOptions
                options = props.availableOptions
                onChange = { store.dispatch(ToggleProjectOption(it)) }
                asText = { it.module.title }
            }
        }
    }

    companion object {
        @Suppress("unused")
        val defaultProps = jsObject<FieldsProps> {
            availableSpecs = emptyList()
            availableTargets = emptyList()
            availableContents = emptyList()
            availableOptions = emptyList()
        }
    }
}

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
}

val fields: RClass<FieldsProps> = rConnect<State, RProps, FieldsProps>({ state, _ ->
    selectedSpec = state.project.spec
    selectedTarget = state.project.outputTarget
    selectedContent = state.project.content
    selectedOptions = state.project.options
})(Fields::class.rClass)
