package components.common.builder

import com.github.gpkg4all.common.*
import components.common.form.checkBoxGroup
import components.common.form.radioGroup
import connectors.extension
import kotlinx.html.DIV
import org.w3c.dom.HTMLElement
import react.*
import react.dom.RDOMBuilder
import react.dom.div
import react.redux.useDispatch
import react.redux.useSelector
import reducer.*
import redux.RAction
import redux.WrapperAction

external interface FieldsProps : RProps {
    var availableSpecs: List<Spec>
    var availableTargets: List<OutputTarget>
    var availableContents: List<ContentTarget>
    var availableOptions: List<ModuleInstance>
    var refExtensions: RMutableRef<HTMLElement?>
}

val fieldsComponent = functionalComponent<FieldsProps>("Fields") { props ->
    val project = useSelector { state: AppState -> state.project }
    div("colset colset-main") {
        div("left") {
            div("col-sticky") {
                conformanceField(props, project)
                optionsField(props, project)
                contentField(props, project)
                packagingField(props, project)
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
}


private fun RDOMBuilder<DIV>.packagingField(props: FieldsProps, project: Project) {
    val dispatch = useDispatch<RAction, WrapperAction>()
    control("Packaging") {
        radioGroup<OutputTarget> {
            name = "packaging"
            selected = project.outputTarget
            options = props.availableTargets
            onChange = { dispatch(UpdateProjectTarget(it)) }
            asText = { it.description }
        }
        fieldInput {
            id = "input-name"
            text = "Name"
            value = project.name
            onChange = { dispatch(UpdateProjectName(it)) }
        }
    }
}

private fun RDOMBuilder<DIV>.contentField(props: FieldsProps, project: Project) {
    val dispatch = useDispatch<UpdateProjectContent, WrapperAction>()
    control("GeoPackage content") {
        radioGroup<ContentTarget> {
            name = "content"
            selected = project.content
            options = props.availableContents
            onChange = { dispatch(UpdateProjectContent(it)) }
            asText = { it.description }
        }
    }
}

private fun RDOMBuilder<DIV>.conformanceField(props: FieldsProps, project: Project) {
    val dispatch = useDispatch<UpdateProjectSpecification, WrapperAction>()
    control("GeoPackage conformance") {
        radioGroup<Spec> {
            name = "conformance"
            selected = project.spec
            options = props.availableSpecs
            onChange = { dispatch(UpdateProjectSpecification(it)) }
            asText = { it.description }
        }
    }
}

private fun RDOMBuilder<DIV>.optionsField(props: FieldsProps, project: Project) {
    val dispatch = useDispatch<ToggleProjectOption, WrapperAction>()
    control("GeoPackage options") {
        checkBoxGroup<ModuleInstance> {
            name = "options"
            selected = project.options
            options = props.availableOptions
            onChange = { dispatch(ToggleProjectOption(it)) }
            asText = { it.module.title }
        }
    }
}

@Suppress("FunctionName")
fun RBuilder.Fields(
    block: FieldsProps.() -> Unit
) = child(fieldsComponent) {
    block(attrs)
}

