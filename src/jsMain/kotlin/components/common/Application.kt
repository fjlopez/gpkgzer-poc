package components.common

import components.common.builder.fields
import components.common.builder.hotkeys
import components.common.extension.extensionDialog
import components.common.form.close
import components.common.layout.header
import components.common.layout.sideLeft
import components.common.layout.sideRight
import config.Configuration
import kotlinx.browser.document
import kotlinx.html.id
import modules.react.toastify.toastContainer
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.dom.div
import react.dom.form
import react.dom.hr
import react.functionalComponent
import react.useRef

interface ApplicationComponentProps : ApplicationStateProps, ApplicationDispatchProps

val applicationComponent = functionalComponent<ApplicationComponentProps> { props ->

    val buttonExtension = useRef<HTMLElement?>(null)

    document.body?.className = props.theme.className

    hotkeys {
        onExtensions = { _: Event -> buttonExtension.current?.click() }
        onGenerate = { _: Event -> buttonExtension.current?.click() }
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
                    refExtension = buttonExtension
                    refGenerate = buttonExtension
                }
            }
            extensionDialog {
                attrs {
                    onClose = { props.onEscape() }
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


