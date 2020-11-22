package components.common.builder

import builders.downloadGeoPackage
import builders.downloadZip
import com.github.gpkg4all.common.OutputTargets
import com.github.gpkg4all.common.Project
import components.utils.functionalComponent
import kotlinx.coroutines.*
import modules.sqljs.SqlJsStatic
import modules.sqljs.initDb
import org.w3c.dom.events.Event
import react.*
import react.redux.useSelector
import reducer.AppState

external interface GenerateProps : RProps

val generateComponent = functionalComponent<GenerateProps>("Generate") { _ ->

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
                primary = true
                disabled = generating
                onClick = onSubmit
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
): Boolean {
    child(generateComponent) {
    }
    return true
}


