package components.common.builder

import builders.downloadGeoPackage
import builders.downloadZip
import com.github.gpkg4all.common.OutputTargets
import com.github.gpkg4all.common.Project
import components.utils.functionalComponent
import components.utils.useWindowsUtils
import kotlinx.coroutines.*
import modules.sqljs.SqlJsStatic
import modules.sqljs.initDb
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.*
import react.redux.useSelector
import reducer.AppState

external interface GenerateProps : RProps {
    var refButton: RMutableRef<HTMLElement?>
}

val generateComponent = functionalComponent<GenerateProps>("Generate") { props ->

    val windowsUtils = useWindowsUtils()
    var loaded by useState(false)
    var initDb by useState<SqlJsStatic?>(null)
    var generating by useState(false)
    val project = useSelector { state: AppState -> state.project }

    useEffectWithCleanup(listOf(loaded)) {
        val scope = CoroutineScope(Dispatchers.Default)
        val cleanup = { scope.cancel() }
        if (!loaded) {
            scope.launch {
                initDb = initDb().await()
                loaded = true
            }
        }
        cleanup
    }

    val onSubmit = { event: Event ->
        event.preventDefault()
        if (loaded && !generating) {
            generating = true
            CoroutineScope(Dispatchers.Default)
                .launch(block = launchGenerator(initDb, project))
                .invokeOnCompletion { generating = false }
        }
    }

    when {
        !loaded -> {
            Button({ disabled = true }) {
                +"Loading SQLite driver ..."
            }
        }
        generating -> Button({ disabled = true }) {
            +"Building GeoPackage ..."
        }
        else -> {
            Button({
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

fun launchGenerator(initDb: SqlJsStatic?, project: Project): suspend CoroutineScope.() -> Unit = {
    when (project.outputTarget) {
        OutputTargets.gpkg -> initDb?.let { downloadGeoPackage(it, project) }
        OutputTargets.zip -> downloadZip(project)
    }
}

@Suppress("FunctionName")
fun RBuilder.Generate(
    ref: RMutableRef<HTMLElement?>
): Boolean {
    child(generateComponent) {
        attrs.refButton = ref
    }
    return true
}


