package components.common

import components.common.builder.hotkeys
import components.common.form.close
import components.common.layout.header
import components.common.layout.sideLeft
import config.Configuration
import connectors.*
import kotlinx.browser.document
import kotlinx.html.id
import modules.react.toastify.toastContainer
import org.w3c.dom.HTMLElement
import react.dom.div
import react.dom.form
import react.dom.hr
import react.functionalComponent
import react.useRef

interface ApplicationComponentProps : ApplicationStateProps, ApplicationDispatchProps

val applicationComponent = functionalComponent<ApplicationComponentProps> { props ->

    val buttonExtensions = useRef<HTMLElement?>(null)
    val buttonGenerate = useRef<HTMLElement?>(null)
    val buttonExplore = useRef<HTMLElement?>(null)

    document.body?.className = props.theme.className

    hotkeys {
        onExtensions = { buttonExtensions.current?.click() }
        onGenerate = { buttonGenerate.current?.click() }
        onExplore = { buttonExplore.current?.click() }
    }
    sideLeft()
    div {
        attrs.id = "main"
        header()
        hr("divider") { }
        form("form") {
            fields {
                attrs {
                    availableSpecs = Configuration.supportedSpecifications
                    availableTargets = Configuration.supportedTargets
                    availableContents = Configuration.supportedContents
                    availableOptions = Configuration.options
                    refExtensions = buttonExtensions
                    refGenerate = buttonGenerate
                    refExplore = buttonExplore
                }
            }
            extensionDialog {
                attrs {
                    onClose = props.onCloseExtensions
                }
            }
            exploreDialog { }
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


