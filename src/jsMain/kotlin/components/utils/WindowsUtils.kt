package components.utils

import kotlinx.browser.window
import org.w3c.dom.events.Event
import react.*

class Window(
    symb: Pair<String, RSetState<String>>,
    origin: Pair<String, RSetState<String>>,
    height: Pair<Int, RSetState<Int>>,
    width: Pair<Int, RSetState<Int>>
) {
    val symb by symb
    val origin by origin
    val height by height
    val width by width
}

fun useWindowsUtils(): Window {
    val isClient = js("typeof window === 'object'").unsafeCast<Boolean>()
    val symb = useState(if (window.navigator.userAgent.toLowerCase().contains("mac")) "⌘" else "Ctrl")
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
    return Window(symb, origin, height, width)
}
