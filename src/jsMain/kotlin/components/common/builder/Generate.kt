package components.common.builder

import builders.generateGeoPackage
import com.github.gpkg4all.common.Project
import components.utils.functionalComponent
import components.utils.useWindowsUtils
import connectors.GenerateProps
import connectors.GenerateStateProps
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.html.dom.create
import kotlinx.html.js.a
import modules.sqljs.SqlJsStatic
import modules.sqljs.initDb
import org.khronos.webgl.Uint8Array
import org.w3c.dom.events.Event
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import react.getValue
import react.setValue
import react.useEffect
import react.useState

external interface GenerateComponentProps : GenerateProps, GenerateStateProps

val generateComponent = functionalComponent<GenerateComponentProps>("Generate") { props ->

    val windowsUtils = useWindowsUtils()
    var loaded by useState(false)
    var initDb by useState<SqlJsStatic?>(null)
    var generating by useState(false)

    useEffect(listOf(loaded)) {
        CoroutineScope(Dispatchers.Default).launch {
            initDb = initDb().await()
            loaded = true
        }
    }

    val onSubmit = { event: Event ->
        event.preventDefault()
        if (loaded && !generating) {
            generating = true
            CoroutineScope(Dispatchers.Default)
                .launch(block = launchGenerator(initDb, props.project))
                .invokeOnCompletion { generating = false }
        }
    }

    when {
        !loaded -> {
            button({ disabled = true }) {
                +"Loading SQLite driver ..."
            }
        }
        generating -> button({ disabled = true }) {
            +"Building GeoPackage ..."
        }
        else -> {
            button({
                id = "generate-project"
                hotkey = windowsUtils.symb + " + I"
                primary = true
                disabled = generating
                onClick = onSubmit
                refButton = props.refButton
            }) {
                +"Build GeoPackage"
            }
        }
    }
}

fun launchGenerator(initDb: SqlJsStatic?, project: Project) : suspend CoroutineScope.()->Unit =
    when {
        initDb == null -> {{}}
        project.spec == null -> {{}}
        else -> {{
            generateGeoPackage(initDb, project.spec) { export: Uint8Array ->
                val blob = Blob(arrayOf(export), BlobPropertyBag(type = "octet/stream"))
                val url = URL.createObjectURL(blob)
                val link = document.create.a()
                link.style.display = "none"
                link.href = url
                link.download = "sqlite.db"
                link.click()
                URL.revokeObjectURL(url)
            }
        }}
    }


