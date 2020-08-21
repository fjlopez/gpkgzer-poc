package modules.react.toastify

import kotlinext.js.require

fun warning(msg: String) {
    toast.warning(msg)
    console.info(msg)
}

@Suppress("NOTHING_TO_INLINE")
inline fun toastRequires() {
    require("react-toastify/dist/ReactToastify.css")
}