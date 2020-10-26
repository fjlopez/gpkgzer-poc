package components.common

import com.github.gpkg4all.common.ProjectDescriptor
import components.common.builder.Button
import components.common.builder.Fields
import components.common.builder.Generate
import components.common.builder.hotkeys
import components.common.explore.Explore
import components.common.extension.ExtensionsDialog
import components.common.layout.SideRight
import components.common.layout.header
import components.common.layout.sideLeft
import components.common.share.Share
import config.Configuration
import config.Configuration.supportedContents
import config.Configuration.supportedOptions
import config.Configuration.supportedSpecifications
import config.Configuration.supportedTargets
import kotlinext.js.js
import kotlinext.js.jsObject
import kotlinx.browser.window
import kotlinx.html.id
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import modules.querystring.parse
import modules.react.toastify.ToastContainerProps
import modules.react.toastify.toast
import modules.react.toastify.toastContainer
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.*
import react.dom.div
import react.dom.form
import react.dom.hr
import react.redux.useDispatch
import react.redux.useSelector
import reducer.AppState
import reducer.LoadExternalConfiguration
import redux.WrapperAction

external interface ApplicationProps : RProps


val applicationComponent = functionalComponent<ApplicationProps>("Application") { _ ->

    val buttonExtensions = useRef<HTMLElement?>(null)
    val buttonGenerate = useRef<HTMLElement?>(null)
    val buttonExplore = useRef<HTMLElement?>(null)

    val dispatch = useDispatch<LoadExternalConfiguration, WrapperAction>()

    var openShare by useState(false)
    var openExplore by useState(false)
    val openExtensionsDialog = useSelector { state: AppState -> state.showExtensionsDialog }

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
            val result = runCatching {
                val params = parse(hash, js { arrayFormat = "bracket" })

                @Suppress("EXPERIMENTAL_API_USAGE")
                val projectDescription = Json.decodeFromDynamic<ProjectDescriptor>(params)
                dispatch(LoadExternalConfiguration(projectDescription))
            }
            if (result.isSuccess) {
                toast.success("Configuration loaded.", jsObject<ToastContainerProps> {
                    autoClose = 3000
                })
            } else {
                toast.error("Configuration not loaded.", jsObject<ToastContainerProps> {
                    autoClose = 3000
                })
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
            Fields {
                availableSpecs = supportedSpecifications
                availableTargets = supportedTargets
                availableContents = supportedContents
                availableOptions = supportedOptions
                refExtensions = buttonExtensions
            }
        }
        div("actions") {
            div("actions-container") {
                Generate(ref = buttonGenerate)
                Button({
                    id = "explore-project"
                    hotkey = "ctrl + space"
                    primary = true
                    onClick = { openExplore = true }
                }) {
                    +"Explore"
                }
                Button({
                    id = "share-project"
                    primary = true
                    onClick = { openShare = true }
                }) {
                    +"Share"
                }
            }
        }
        openExtensionsDialog && ExtensionsDialog()
        openShare && Share(onClose = { openShare = false })
        openExplore && Explore(onClose = { openExplore = false })
    }
    SideRight(theme = Configuration.theme)
    toastContainer {
        attrs {
            position = "top-center"
            hideProgressBar = true
        }
    }
}

@Suppress("FunctionName")
fun RBuilder.Application(
): Boolean {
    child(applicationComponent) {}
    return true
}


