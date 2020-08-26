package components.common

import components.common.builder.fields
import components.common.builder.hotkeys
import components.common.extension.extensionDialog
import components.common.form.close
import components.common.layout.header
import components.common.layout.sideLeft
import components.common.layout.sideRight
import components.utils.functionalComponent
import components.utils.invoke
import config.Configuration
import kotlinx.browser.document
import modules.react.toastify.toastContainer
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.RClass
import react.RProps
import react.dom.div
import react.dom.form
import react.dom.hr
import react.redux.rConnect
import react.useRef
import reducer.CloseExtensions
import reducer.State
import reducer.store

interface ApplicationProps : RProps {
    var theme: Theme
}

val applicationComponent = functionalComponent<ApplicationProps>("Application") { props ->
    val buttonExtension = useRef<HTMLElement?>(null)

    val onEscape: (Event) -> Unit = {
        store.dispatch(CloseExtensions)
    }

    document.body?.className = props.theme.className

    hotkeys {
        onExtensions = { _: Event -> buttonExtension.current?.click() }
    }
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
                    refExtension = buttonExtension
                }
            }
            extensionDialog {
                attrs {
                    onClose = onEscape
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

val application: RClass<ApplicationProps> = rConnect<State, RProps, ApplicationProps>({ state, _ ->
    theme = state.theme
})(applicationComponent)

