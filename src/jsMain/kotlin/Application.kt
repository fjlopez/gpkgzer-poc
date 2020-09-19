import components.common.app
import kotlinext.js.require
import kotlinx.browser.document
import modules.react.toastify.toastRequires
import react.dom.render
import react.redux.provider
import reducer.store

fun main() {
    require("styles/app.scss")
    toastRequires()
    val rootDiv = document.getElementById("app-root")
    render(rootDiv) {
        provider(store) {
            app()
        }
    }
}




