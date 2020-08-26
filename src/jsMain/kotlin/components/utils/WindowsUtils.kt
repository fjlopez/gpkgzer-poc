package components.utils

import kotlinx.browser.window
import org.w3c.dom.events.Event
import react.RCleanup
import react.useEffectWithCleanup
import react.useState

data class Window(
    val symb: String,
    val origin: String,
    val height: Int,
    val width: Int
)

fun useWindowsUtils(): Window {
    val isClient = js("typeof window === 'object'").unsafeCast<Boolean>()
    val symb = useState(
        if (window.navigator.userAgent.toLowerCase().contains("mac")) "⌘" else "Ctrl"
    )
    val origin = useState(window.location.origin)
    val height = useState(window.innerHeight)
    val width = useState(window.innerWidth)

    useEffectWithCleanup(listOf(isClient)) {
        fun cleanup(handle: (Event) -> Unit): RCleanup = { window.removeEventListener("resize", handle) }
        if (isClient) {
            val handleResize = { _: Event ->
                height.second(window.innerHeight)
                width.second(window.innerWidth)
            }
            window.addEventListener("resize", handleResize)
            cleanup(handleResize)
        } else { -> }
    }
    return Window(symb.first, origin.first, height.first, width.first)
}
