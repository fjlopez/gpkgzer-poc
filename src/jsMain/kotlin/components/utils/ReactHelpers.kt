package components.utils

import kotlinx.html.LABEL
import react.*
import react.dom.RDOMBuilder

/**
 * Get a React Connect class connected to a functional [component].
 */
fun <P : RProps, R : RProps> HOC<P, R>.connects(
    displayName: String? = null,
    defaultProps: P? = null,
    component: FunctionalComponent<P>
): RClass<R> {
    val rc = call(null, { props: P ->
        component(props)
    })
    val wrapped = rc.asDynamic().WrappedComponent.unsafeCast<RClass<P>>()
    val wrappedName = wrapped.asDynamic().displayName.unsafeCast<String?>()

    rc.asDynamic().displayName = "RConnect (${displayName ?: wrappedName ?: "Component"})"

    if (displayName != null) {
        wrapped.asDynamic().displayName = displayName
    }
    if (defaultProps != null) {
        wrapped.asDynamic().defaultProps = defaultProps
    }
    return rc
}

/**
 * Get functional component from [func].
 *
 * This function wraps [react.functionalComponent].
 */
fun <P : RProps> functionalComponent(
    displayName: String? = null,
    defaultProps: P? = null,
    func: RBuilder.(props: P) -> Unit
): FunctionalComponent<P> {
    val fc = functionalComponent(displayName, func)
    if (defaultProps != null) {
        fc.asDynamic().defaultProps = defaultProps
    }
    return fc
}

/**
 * In react "htmlFor" is used instead "for" because it is a JS reserved keyword.
 */
fun RDOMBuilder<LABEL>.attrsHtmlFor(label: String?) = label?.let { attrs["htmlFor"] = it }
