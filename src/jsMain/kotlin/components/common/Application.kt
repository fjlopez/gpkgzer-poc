package components.common

import components.common.builder.fields
import components.common.form.close
import components.common.layout.header
import components.common.layout.sideLeft
import components.common.layout.sideRight
import config.Configuration
import kotlinx.browser.document
import modules.react.toastify.toastContainer
import react.*
import react.dom.div
import react.dom.form
import react.dom.hr
import react.redux.rConnect
import reducer.State

class ApplicationProps : RProps {
    lateinit var theme: Theme
}

class Application : RComponent<ApplicationProps, RState>() {

    override fun RBuilder.render() {
        document.body?.className = props.theme.className
        sideLeft()
        div {
            attrs["id"] = "main"
            header()
            hr("divider") { }
            form("form") {
                fields {
                    attrs {
                        availableSpecs = Configuration.supportedSpecifications
                        availableTargets = Configuration.supportedTargets
                        availableContents = Configuration.supportedContents
                        availableOptions = Configuration.options
                    }
                }
            }
        }
        sideRight { }
        toastContainer {
            attrs {
                position = "top-center"
                hideProgressBar = true
                closeButton = close { }
            }
        }
    }
}

val application: RClass<ApplicationProps> = rConnect<State, RProps, ApplicationProps>({ state, _ ->
    theme = state.theme
})(Application::class.rClass)


