package components.common

import com.github.gpkg4all.common.ProjectDescriptor
import components.common.builder.Button
import components.common.builder.hotkeys
import components.common.form.close
import components.common.layout.SideRight
import components.common.layout.header
import components.common.layout.sideLeft
import config.Configuration
import connectors.*
import kotlinx.browser.window
import kotlinx.html.id
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import modules.querystring.parse
import modules.react.toastify.toastContainer
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.*
import react.dom.div
import react.dom.form
import react.dom.hr

interface ApplicationComponentProps : ApplicationStateProps, ApplicationDispatchProps

val applicationComponent = functionalComponent<ApplicationComponentProps> { props ->

    val buttonExtensions = useRef<HTMLElement?>(null)
    val buttonGenerate = useRef<HTMLElement?>(null)
    val buttonExplore = useRef<HTMLElement?>(null)

    var showShare by useState(false)

    var hash by useState(window.location.hash)

    useEffectWithCleanup {
        val handler = { _: Event -> hash = window.location.hash }
        val cleanup = { window.removeEventListener("hashchange", handler) }
        window.addEventListener("hashchange", handler)
        cleanup
    }

    useEffect(listOf(hash)) {
        if (hash.isNotEmpty()) {
            window.history.pushState(null, "", window.location.pathname)
            runCatching {
                val params = parse(hash)

                @Suppress("EXPERIMENTAL_API_USAGE")
                val projectDescription = Json.decodeFromDynamic<ProjectDescriptor>(params)
                // project = build from projectDescription
            }.onSuccess {
                // dispatch new project description
                // if we are here toast.success("Configuration loaded")
            }.onFailure {
                // if we are here toast.error("Configuration not loaded")
            }
            hash = ""
        }
    }

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
                }
            }
        }
        div("actions") {
            div("actions-container") {
                generate(buttonGenerate)
                explore(buttonExplore)
                Button({
                    id = "share-project"
                    primary = true
                    onClick = { showShare = true }
                }) {
                    +"Share"
                }
            }
        }
        shareDialog(showShare) { showShare = false }
        extensionDialog(props.onEscape)
        exploreDialog()
    }
    SideRight(Configuration.theme)
    toastContainer {
        attrs {
            position = "top-center"
            hideProgressBar = true
            closeButton = close { }
        }
    }
}


