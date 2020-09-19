package components.common.builder

import components.common.extension.ExtensionProps
import components.utils.functionalComponent
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.html.dom.create
import kotlinx.html.js.a
import modules.sqljs.SqliteDriver
import modules.sqljs.initDb
import modules.sqljs.invoke
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import react.*

external interface GenerateProps : RProps {
    var refButton: RMutableRef<HTMLElement?>
}

private val generateComponent = functionalComponent<GenerateProps>("Generate") { props ->

    var loaded by useState(false)
    var db by useState<SqliteDriver?>(null)
    var generating by useState(false)

    useEffect(listOf(loaded)) {
        CoroutineScope(Dispatchers.Default).launch {
            val initDb = initDb().await()
            db = SqliteDriver(initDb.Database.invoke())
            loaded = true
        }
    }

    val onSubmit = { event: Event ->
        event.preventDefault()
        if (!generating) {
            generating = true
            CoroutineScope(Dispatchers.Default).launch {
                db?.export()?.let { export ->
                    val blob = Blob(arrayOf(export), BlobPropertyBag(type = "octet/stream"))
                    val url = URL.createObjectURL(blob)
                    val link = document.create.a()
                    link.style.display = "none"
                    link.href = url
                    link.download = "sqlite.db"
                    link.click()
                    URL.revokeObjectURL(url)
                }
                generating = false
            }
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

fun RBuilder.generate(props: GenerateProps) = child(generateComponent, props)
