/**
 * See https://fkhadra.github.io/react-toastify/introduction
 */

@file:JsModule("react-toastify")
@file:JsNonModule

package modules.react.toastify

import react.RClass
import react.RProps

@JsName("ToastContainer")
external val toastContainer: RClass<ToastContainerProps>

@JsName("toast")
external val toast: dynamic

external interface ToastContainerProps : RProps {
    var position: String?
    var hideProgressBar: Boolean?
    var closeButton: dynamic
    var autoClose: dynamic
    var limit: Int
}
