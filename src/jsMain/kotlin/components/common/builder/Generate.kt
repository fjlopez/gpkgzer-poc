package components.common.builder

import builders.buildGeoPackage
import builders.buildZip
import com.github.gpkg4all.common.*
import components.utils.functionalComponent
import components.utils.saveAs
import components.utils.useWindowsUtils
import connectors.GenerateProps
import connectors.GenerateStateProps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import modules.sqljs.SqlJsStatic
import modules.sqljs.initDb
import org.khronos.webgl.Uint8Array
import org.w3c.dom.events.Event
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

fun launchGenerator(initDb: SqlJsStatic?, project: Project): suspend CoroutineScope.() -> Unit =
    when {
        initDb == null -> {
            {}
        }
        project.spec == null -> {
            {}
        }
        else -> {
            {
                when(project.outputTarget) {
                    OutputTargets.gpkg -> buildGeoPackage(initDb, project.spec) { export: Uint8Array ->
                        saveAs(export, project.name + ".gpkg")
                    }
                    OutputTargets.zip -> buildZip(project.spec) {
                        if (it is Uint8Array) {
                            saveAs(it.unsafeCast<Uint8Array>(), project.name + ".zip")
                        }
                    }
                }
            }
        }
    }


