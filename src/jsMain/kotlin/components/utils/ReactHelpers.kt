package components.utils

import kotlinext.js.Object
import kotlinext.js.jsObject
import kotlinx.html.LABEL
import react.*
import react.dom.RDOMBuilder

operator fun <P : RProps, R : RProps> HOC<P, R>.invoke(
    component: FunctionalComponent<P>,
    displayName: String? = null,
    defaultProps: P.() -> Unit = {}
): RClass<R> =
    call(null, { props: P ->
        component(props)
    }).apply {
        asDynamic().displayName = displayName?.let { "RConnect ($displayName)" } ?: "RConnect (Component)"
        asDynamic().WrappedComponent.unsafeCast<RClass<P>>().apply {
            this.displayName = displayName ?: "Anonymous"
            this.defaultProps = kotlinext.js.js(defaultProps).unsafeCast<P>()
        }
    }


fun <P : RProps> functionalComponent(
    displayName: String,
    defaultProps: P? = null,
    func: RBuilder.(props: P) -> Unit
): FunctionalComponent<P> = functionalComponent(func).also {
    Object.assign(
        it.unsafeCast<RClass<P>>(),
        jsObject {
            this.displayName = displayName
            this.defaultProps = defaultProps
        })
}

/**
 * In react "htmlFor" is used instead "for" because it is a JS reserved keyword.
 */
fun RDOMBuilder<LABEL>.attrsHtmlFor(label: String?) = label?.let { attrs["htmlFor"] = it }
