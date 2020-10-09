package components.common.builder

import com.github.gpkg4all.common.ContentTarget
import com.github.gpkg4all.common.ModuleInstance
import com.github.gpkg4all.common.OutputTarget
import com.github.gpkg4all.common.Spec
import components.common.form.checkBoxGroup
import components.common.form.radioGroup
import connectors.*
import kotlinx.html.DIV
import react.*
import react.dom.RDOMBuilder
import react.dom.div

external interface FieldsComponentProps : FieldsProps, FieldsStateProps, FieldsDispatchProps

val fieldsComponent = functionalComponent<FieldsComponentProps> { props ->
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
                attrs {
                    refButton = props.refExtensions
                }
            }
        }
    }
    div("actions") {
        div("actions-container") {
            generate {
                attrs {
                    refButton = props.refGenerate
                }
            }
            explore {
                attrs {
                    refButton = props.refExplore
                }
            }
        }
    }
}


private fun RDOMBuilder<DIV>.packagingField(props: FieldsComponentProps) {
    control("Packaging") {
        radioGroup<OutputTarget> {
            name = "packaging"
            selected = props.selectedTarget
            options = props.availableTargets
            onChange = props.onChangeTarget
            asText = { it.description }
        }
    }
}

private fun RDOMBuilder<DIV>.contentField(props: FieldsComponentProps) {
    control("GeoPackage content") {
        radioGroup<ContentTarget> {
            name = "content"
            selected = props.selectedContent
            options = props.availableContents
            onChange = props.onChangeContent
            asText = { it.description }
        }
    }
}

private fun RDOMBuilder<DIV>.conformanceField(props: FieldsComponentProps) {
    control("GeoPackage conformance") {
        radioGroup<Spec> {
            name = "conformance"
            selected = props.selectedSpec
            options = props.availableSpecs
            onChange = props.onChangeSpecs
            asText = { it.description }
        }
    }
}

private fun RDOMBuilder<DIV>.optionsField(props: FieldsComponentProps) {
    control("GeoPackage options") {
        checkBoxGroup<ModuleInstance> {
            name = "options"
            selected = props.selectedOptions
            options = props.availableOptions
            onChange = props.onChangeOptions
            asText = { it.module.title }
        }
    }
}

